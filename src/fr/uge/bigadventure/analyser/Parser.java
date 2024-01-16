package fr.uge.bigadventure.analyser;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import fr.uge.bigadventure.element.Behavior;
import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.GridElement;
import fr.uge.bigadventure.element.Kind;
import fr.uge.bigadventure.element.Obstacle;
import fr.uge.bigadventure.element.Weapon;

public class Parser {

	private static final Pattern SIZE_PATTERN = Pattern.compile(":\\((\\d+)x(\\d+)");
	private static final Pattern ENCODING_PATTERN = Pattern.compile("([A-Z]+)\\(([A-Z])\\)");
	private static final Pattern ELEMENT_STRING = Pattern.compile(":([a-zA-Z]+)");
	private static final Pattern ELEMENT_BOOL = Pattern.compile(":(true|false)");
	private static final Pattern ELEMENT_POSITION = Pattern.compile(":\\((\\d+),(\\d+)");
	private static final Pattern ELEMENT_INT = Pattern.compile(":(\\d+)");
	private static final Pattern ELEMENT_ZONE = Pattern.compile(":\\((\\d+),(\\d+)\\)\\((\\d+)x(\\d+)\\)");

	private static String lineError(Result result ,String message) {
		return message + " at line " + result.lineNo();
	}
	
	/** The general method to parse a .map file
	 * It calls sub-methods depending of what the iterator on the file finds.
	 * 
	 */
	public static GridElement[][] parse(Lexer lexer) {
		Objects.requireNonNull(lexer);
		Result result;
		GridElement[][] grid = null; var elementList = new ArrayList<Element>();
		while((result = lexer.nextResult()) != null) {
			switch(result.content()) {
				case "grid" -> grid = parseGrid(lexer);
				case "element" -> elementList.add(parseElement(lexer));
			};	
		}
		return grid;
	}

	/** Redirects the parsing to the dedicated parsing method depending of the identifier
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 */
	private static GridElement[][] parseGrid(Lexer lexer) {
		Objects.requireNonNull(lexer);		
		Result result;
		if((result = lexer.nextResult()).token() != Token.RIGHT_BRACKET) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of [grid] instruction"));
		}
		Point size = null; HashMap<Character,String> encodings = null;
		while((result = lexer.nextResult()).token() != Token.QUOTE) {
      switch(result.content()) {
        case "size" -> size = parseMapSize(lexer);
        case "encodings" -> encodings = parseMapEncodings(lexer);
      };
		}
    var grid = parseMapData(result, size, encodings);
    return grid;
	}

	/** Parses the main section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A point with width of the grid as x and height of the grid as y
	 */
	private static Point parseMapSize(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var sizeBuilder = new StringBuilder();
		Result result;
		while((result = lexer.nextResult()).token() != Token.RIGHT_PARENS) {
			sizeBuilder.append(result.content());
		}
		var sizeString = sizeBuilder.toString();
		var m = SIZE_PATTERN.matcher(sizeString);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Size string does not match with regex"));
		}
		int width = Integer.parseInt(m.group(1)); int height = Integer.parseInt(m.group(2));
		return new Point(width, height);	
	}
	
	/** Parses the encodings section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A map associating a character with a skin
	 */
	private static HashMap<Character, String> parseMapEncodings(Lexer lexer) {	
		Objects.requireNonNull(lexer);
		var encodingsBuilder = new StringBuilder();
		Result result = lexer.nextResult();
		while((result.content().equals(result.content().toUpperCase(Locale.ROOT)))) {
			encodingsBuilder.append(result.content());
			result = lexer.nextResult();
		}
		var encodingString = encodingsBuilder.toString().substring(1);
		var m = ENCODING_PATTERN.matcher(encodingString);
		var encodingsMap = new HashMap<Character, String>();
		while(m.find()) {
			var skin = GridElement.checkSkinFile(m.group(1).toLowerCase(Locale.ROOT));
			if(encodingsMap.putIfAbsent(m.group(2).charAt(0), skin) != null) {
				throw new IllegalStateException(lineError(result, m.group(2) + " symbol is already defined as " + m.group(1)));
			}
		}
		if(encodingsMap.isEmpty()) {
			throw new IllegalStateException("The encodings map should not be empty");
		}
		System.out.println(encodingsMap);
		return encodingsMap;	
	}
	
	/** Parses the data section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A 2D Array of Obstacle and Decoration
	 */
	private static GridElement[][] parseMapData(Result quote, Point size, HashMap<Character, String> encodings) {
		Objects.requireNonNull(quote);
		if(quote.token() != Token.QUOTE) {
			throw new IllegalArgumentException();
		}
		Objects.requireNonNull(size);
		Objects.requireNonNull(encodings);
		var gridList = Arrays.asList(quote.content().split("\n")).stream().map(s -> s.trim()).toList();
		if(!(gridList.get(0).equals("\"\"\"") && gridList.get(gridList.size() - 1).equals("\"\"\""))) {
			throw new IllegalArgumentException(lineError(quote, "Data string does not match with regex"));
		}
		gridList = gridList.subList(1, gridList.size() - 1);
		if(gridList.size() != size.y) {
			throw new IllegalStateException(lineError(quote, "The grid is not at the right height"));
		}
		var dataPattern = Pattern.compile("([A-Z]| )");
		var grid = new GridElement[size.y][size.x];
		for(var row : gridList) {
			var m = dataPattern.matcher(row);
			var rowIndex = gridList.indexOf(row);
			var column = 0;
			while(m.find()) {
				if(!m.group().isBlank()) {
					grid[rowIndex][column] = new Obstacle(encodings.get(m.group().charAt(0)), new Point(column, rowIndex));
				}
				column++;	
			}
		}
		return grid;
	}

	private static Element parseElement(Lexer lexer) {
		Objects.requireNonNull(lexer);		
		Result result;
		if((result = lexer.nextResult()).token() != Token.RIGHT_BRACKET) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of [element] instruction"));
		}
		String name = null; boolean player, phantomized; 
		Point position = null;
		int health, damage;
		Kind kind = null; Behavior behavior = null; List<Point> zone = null;
		while((result = lexer.nextResult()) != null) {
			if(result.token() == Token.LEFT_BRACKET) {
				break;
			}
      switch(result.content()) {
      	case "name" -> name = parseElementName(lexer);
//      	case "skin" ->
      	case "player" -> player = parseElementBool(lexer);
      	case "position" -> position = parseElementPosition(lexer);
      	case "health" -> health = parseElementInt(lexer);
      	case "kind" -> kind = parseElementKind(lexer); 
      	case "zone" -> zone = parseElementZone(lexer);
      	case "behavior" -> behavior = parseElementBehavior(lexer);
      	case "damage" -> damage = parseElementInt(lexer);
      	// case "text" ->
      	// case "steal" ->
      	// case "trade" ->
      	// case "locked" ->
      	// case "flow" ->
      	case "phantomized" -> phantomized = parseElementBool(lexer);
      	// case "teleport" ->
      	
      };
		}
		System.out.println(zone);
		return new Weapon("sword", "word", new Point(0,0), 10); //debug
	}

	private static String parseElementName(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of name property"));
		}
		return m.group(1);
	}
	
	private static boolean parseElementBool(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_BOOL.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of player property"));
		}
		return Boolean.parseBoolean(m.group(1));
	}
	
	private static Point parseElementPosition(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		Result result;
		while((result = lexer.nextResult()).token() != Token.RIGHT_PARENS) {
			propertyBuilder.append(result.content());
		}
		var m = ELEMENT_POSITION.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of position property"));
		}
		return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));	
	}
	
	private static int parseElementInt(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_INT.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of damage/health property"));
		}
		return Integer.parseInt(m.group(1));
	}

	private static Kind parseElementKind(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of kind property"));
		}
		return switch(m.group(1)) {
			case "friend" -> Kind.FRIEND; 
			case "enemy" -> Kind.ENEMY;
			case "item" -> Kind.ITEM;
			case "obstacle" -> Kind.OBSTACLE;
			default -> throw new IllegalArgumentException(lineError(result, "Wrong kind name : " + m.group(1)));
		};
	}

	private static Behavior parseElementBehavior(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of behavior property"));
		}
		return switch(m.group(1)) {
			case "shy" -> Behavior.SHY; 
			case "stroll" -> Behavior.STROLL;
			case "agressive" -> Behavior.AGRESSIVE;
			default -> throw new IllegalArgumentException(lineError(result, "Wrong behavior name : " + m.group(1)));
		};
	}
	
	private static List<Point> parseElementZone(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		// On doit avoir 11 lexemes pour zone
		Result result = lexer.nextResult();
		for(int n = 0; n < 11; n++) {
			propertyBuilder.append(result.content());
			if(n != 10) result = lexer.nextResult(); // Pour eviter de manger le Result de la ligne suivante
		}
		System.out.println(result);
		var m = ELEMENT_ZONE.matcher(propertyBuilder);
		if(!m.matches()) {
			throw new IllegalArgumentException(lineError(result, "Wrong format of zone property"));
		}
		var topLeftOfZone = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
		var bottomRightOfZone = new Point(topLeftOfZone.x + Integer.parseInt(m.group(3)), topLeftOfZone.y + Integer.parseInt(m.group(4)));
		return List.of(topLeftOfZone, bottomRightOfZone);
	}
	
  public static void main(String[] args) throws IOException {
    var path = Path.of("maps/big.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    parse(lexer);
//    Result result;
//    while((result = lexer.nextResult()) != null) {
//      System.out.println(result);
//    }
  }
	
    
	
}

package fr.uge.bigadventure.analyser;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import fr.uge.bigadventure.GameMap;
import fr.uge.bigadventure.element.Behavior;
import fr.uge.bigadventure.element.Decoration;
import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Friend;
import fr.uge.bigadventure.element.GridElement;
import fr.uge.bigadventure.element.InventoryItem;
import fr.uge.bigadventure.element.Item;
import fr.uge.bigadventure.element.Kind;
import fr.uge.bigadventure.element.Obstacle;
import fr.uge.bigadventure.element.Player;
import fr.uge.bigadventure.element.Weapon;

public class Parser {
	
	private static final Pattern SIZE_PATTERN = Pattern.compile(":\\((\\d+)x(\\d+)");
	private static final Pattern ENCODING_PATTERN = Pattern.compile("([A-Z]+)\\(([A-Z]|[a-z])\\)");
	private static final Pattern ELEMENT_STRING = Pattern.compile(":([a-zA-Z]+)");
	private static final Pattern ELEMENT_BOOL = Pattern.compile(":(true|false)");
	private static final Pattern ELEMENT_POSITION = Pattern.compile(":\\((\\d+),(\\d+)");
	private static final Pattern ELEMENT_INT = Pattern.compile(":(\\d+)");
	private static final Pattern ELEMENT_ZONE = Pattern.compile(":\\((\\d+),(\\d+)\\)\\((\\d+)x(\\d+)\\)");

	private static int errorCount = 0;
	
	/** Gives an error message that indicates the line of the error while parsing 
	 * 
	 * @param result the result from which to extract the line number
	 * @param message the error message to be followed by the line number
	 * @return a concatenated String of error message and line number
	 */
	public static String lineError(Result result ,String message) {
		errorCount++;
		return message + " at line " + result.lineNo();
	}
	
	/** The general method to parse a .map file
	 * It calls sub-methods depending of what the iterator on the file finds.
	 * @param lexer The lexer corresponding to the .map file
	 * @return A GameMap containing the grid and the list of Element
	 */
	public static GameMap parse(Lexer lexer) {
		Objects.requireNonNull(lexer);
		Result result;
		GridElement[][] grid = null; var elementList = new ArrayList<Element>();
		while((result = lexer.nextResult()) != null) {
			switch(result.content()) {
				case "grid" -> grid = parseGrid(lexer);
				case "element" -> elementList.add(parseElement(grid, lexer));
			};	
		}
		elementList.removeIf(Objects::isNull);
		var gameMap = new GameMap(grid, elementList);
		if(errorCount == 0) {
			System.out.println("Parsing success");
		} else {
			System.err.println("Parsed with " + errorCount + " errors");
		}
		return gameMap;
	}

	/** Parses the grid section of a .map
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return The grid of Obstacle or Decoration parsed 
	 */
	private static GridElement[][] parseGrid(Lexer lexer) {
		Objects.requireNonNull(lexer);		
		Result result;
		if((result = lexer.nextResult()).token() != Token.RIGHT_BRACKET) {
			System.err.println(lineError(result, "Wrong format of [grid] instruction"));
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

	/** Parses the size line of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
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
			System.err.println(lineError(result, "Size string does not match with regex"));
		}
		int width = Integer.parseInt(m.group(1)); int height = Integer.parseInt(m.group(2));
		return new Point(width, height);	
	}
	
	/** Parses the encodings line of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return A map associating a character with a skin partial path
	 */
	private static HashMap<Character, String> parseMapEncodings(Lexer lexer) {	
		Objects.requireNonNull(lexer);
		var encodingsBuilder = new StringBuilder();
		Result result = lexer.nextResult();
		while((result.content().equals(result.content().toUpperCase(Locale.ROOT))) || result.content().length() == 1) {
			encodingsBuilder.append(result.content());
			result = lexer.nextResult();
		}
		
		var encodingString = encodingsBuilder.toString().substring(1);
		var m = ENCODING_PATTERN.matcher(encodingString);
		var encodingsMap = new HashMap<Character, String>();
		while(m.find()) {
			var skin = GridElement.checkSkinFile(result, m.group(1).toLowerCase(Locale.ROOT));
			if(encodingsMap.putIfAbsent(m.group(2).charAt(0), skin) != null) {
				System.err.println(lineError(result, m.group(2) + " symbol is already defined as " + m.group(1)));
			}
		}
		if(encodingsMap.isEmpty()) {
			System.err.println("The encodings map should not be empty");
		}
		return encodingsMap;	
	}
	
	/** Parses the data section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return A 2D Array of Obstacle and Decoration
	 */
	private static GridElement[][] parseMapData(Result quote, Point size, HashMap<Character, String> encodings) {
		Objects.requireNonNull(quote);
		if(Objects.isNull(size)) System.err.println(lineError(quote, "there is no size line in the file!"));
		if(Objects.isNull(encodings)) System.err.println(lineError(quote, "there is no encodings line in the file!"));
		var gridList = Arrays.asList(quote.content().split("\n")).stream().map(s -> s.trim()).toList();
		if(!(gridList.get(0).equals("\"\"\"") && gridList.get(gridList.size() - 1).equals("\"\"\""))) {
			System.err.println(lineError(quote, "Data string does not match with regex"));
		}
		gridList = gridList.subList(1, gridList.size() - 1);
		if(gridList.size() != size.y) {
			System.err.println(lineError(quote, "The grid is not at the right height"));
		}
		var dataPattern = Pattern.compile("([A-Z]|[a-z]| )");
		var grid = new GridElement[size.x][size.y];
		for(int i = 0; i < gridList.size(); i++) {
			var m = dataPattern.matcher(gridList.get(i));
			var column = 0;
			while(m.find()) {
				if(!m.group().isBlank()) {
					var skin = encodings.get(m.group().charAt(0));
					if(skin.startsWith("obstacle/")) {
						grid[column][i] = new Obstacle(encodings.get(m.group().charAt(0)), new Point(column, i));
					} else {
						grid[column][i] = new Decoration(encodings.get(m.group().charAt(0)), new Point(column, i));
					}
				}
				column++;	
			}
			if(column != size.x) {
				System.err.println(lineError(quote, "The grid is not at the right width"));
			}
		}
		return grid;
	}

	/** Parses an element of a .map file
	 * 
	 * @param grid The grid parsed via the .map file
	 * @param lexer The lexer corresponding to the .map file
	 * @return An Element to be added to the list of Element
	 */
	private static Element parseElement(GridElement[][] grid, Lexer lexer) {
		Objects.requireNonNull(lexer);		
		Result result;
		if((result = lexer.nextResult()).token() != Token.RIGHT_BRACKET) {
			System.err.println(lineError(result, "Wrong format of [element] instruction"));
		}
		boolean supported = true;
		String name = null; String skin = null;
		boolean player = false; //phantomized; 
		Point position = null;
		int health = 0, damage = 0;
		Kind kind = null; Behavior behavior = null; List<Point> zone = null;
		while((result = lexer.nextResult()) != null) {
			if(result.token() == Token.LEFT_BRACKET) {
				break;
			}
      switch(result.content()) {
      	case "name" -> name = parseElementName(lexer);
      	case "skin" -> skin = parseElementName(lexer);
      	case "player" -> player = parseElementBool(lexer);
      	case "position" -> position = parseElementPosition(lexer);
      	case "health" -> health = parseElementInt(lexer);
      	case "kind" -> kind = parseElementKind(lexer); 
      	case "zone" -> zone = parseElementZone(lexer);
      	case "behavior" -> behavior = parseElementBehavior(lexer);
      	case "damage" -> damage = parseElementInt(lexer);
      	case "text" -> { supported = false; System.err.println(lineError(result, "text not supported")); }
      	case "steal" -> { supported = false; System.err.println(lineError(result, "steal not supported")); }
      	case "trade" -> { supported = false; System.err.println(lineError(result, "trade not supported")); }
      	case "locked" -> { supported = false; System.err.println(lineError(result, "locked not supported")); }
      	case "flow" -> { supported = false; System.err.println(lineError(result, "flow not supported")); }
      	case "phantomized" -> { supported = false; System.err.println(lineError(result, "phantomized not supported")); }
      	case "teleport" -> { supported = false; System.err.println(lineError(result, "teleport not supported")); }
      	
      };
		}
		if(!supported) return null;
		if(player) {
			skin = "pnj/" + skin.toLowerCase(Locale.ROOT);
			return new Player(name, skin, health, position);
		}
		return switch(kind) {			 
			case FRIEND -> new Friend(name, "pnj/" + skin.toLowerCase(Locale.ROOT), health, position); 
			case ENEMY -> new Enemy(name, "pnj/" + skin.toLowerCase(Locale.ROOT), health, position, zone, damage, behavior);
			case ITEM -> parseItem(name, "item/" + skin.toLowerCase(Locale.ROOT), position, damage);
			case OBSTACLE -> {
				if(grid[position.x][position.y] == null) {
					grid[position.x][position.y] = new Obstacle("obstacle/" + skin.toLowerCase(Locale.ROOT), position);
				}
				yield null;
			}
		};
	}
	
	/** Determine if an Item is a Weapon or just an InventoryItem. 
	 * 
	 * @param name The name of the item
	 * @param skin The partial skin path of the item
	 * @param position The position in the grid of the item
	 * @param damage The damage : zero or less for an inventory item, else a weapon 
	 * @return a Weapon or InventoryItem to be added to the list of Element
	 */
	private static Item parseItem(String name, String skin, Point position, int damage) {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(damage <= 0) {
			return new InventoryItem(skin, position, 5);
		}
		return new Weapon(name, skin, damage, position);
	}
	
	/** Parses the name line of element section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a String containing the name parsed
	 */
	private static String parseElementName(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of name property"));
		}
		return m.group(1);
	}

	/** Parses a line of element section of a .map file that takes a boolean as argument
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a parsed boolean
	 */
	private static boolean parseElementBool(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_BOOL.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of player property"));
		}
		return Boolean.parseBoolean(m.group(1));
	}

	/** Parses the position line of the element section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a point with the coordinates of the position in argument
	 */
	private static Point parseElementPosition(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		Result result;
		while((result = lexer.nextResult()).token() != Token.RIGHT_PARENS) {
			propertyBuilder.append(result.content());
		}
		var m = ELEMENT_POSITION.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of position property"));
		}
		return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));	
	}

	/** Parses a line of element section of a .map file that takes an int as argument
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a parsed int
	 */
	private static int parseElementInt(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_INT.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of damage/health property"));
		}
		return Integer.parseInt(m.group(1));
	}

	/** Parses the kind line of element section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a Kind, that is an enum value
	 */
	private static Kind parseElementKind(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of kind property"));
		}
		return switch(m.group(1)) {
			case "friend" -> Kind.FRIEND; 
			case "enemy" -> Kind.ENEMY;
			case "item" -> Kind.ITEM;
			case "obstacle" -> Kind.OBSTACLE;
			default -> {
				System.err.println(lineError(result, "Wrong kind name : " + m.group(1)));
				yield null;
			}
		};
	}

	/** Parses the behavior line of element section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a Behavior, that is an enum value
	 */
	private static Behavior parseElementBehavior(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		propertyBuilder.append(lexer.nextResult().content());
		var result = lexer.nextResult();
		propertyBuilder.append(result.content());
		var m = ELEMENT_STRING.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of behavior property"));
		}
		return switch(m.group(1)) {
			case "shy" -> Behavior.SHY; 
			case "stroll" -> Behavior.STROLL;
			case "agressive" -> Behavior.AGRESSIVE;
			default -> { 
				System.err.println(lineError(result, "Unknown behavior name : " + m.group(1)));
				yield null;
			}
		};
	}
	
	/** Parses the zone line of element section of a .map file
	 * 
	 * @param lexer The lexer corresponding to the .map file
	 * @return a two Point list, that contains the top left and the bottom right point of the zone.
	 */
	private static List<Point> parseElementZone(Lexer lexer) {
		Objects.requireNonNull(lexer);
		var propertyBuilder = new StringBuilder();
		// On doit avoir 11 lexemes pour zone
		Result result = lexer.nextResult();
		for(int n = 0; n < 11; n++) {
			propertyBuilder.append(result.content());
			if(n != 10) result = lexer.nextResult(); // Pour eviter de manger le Result de la ligne suivante
		}
		var m = ELEMENT_ZONE.matcher(propertyBuilder);
		if(!m.matches()) {
			System.err.println(lineError(result, "Wrong format of zone property"));
		}
		var topLeftOfZone = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
		var bottomRightOfZone = new Point(topLeftOfZone.x + Integer.parseInt(m.group(3)), topLeftOfZone.y + Integer.parseInt(m.group(4)));
		return List.of(topLeftOfZone, bottomRightOfZone);
	}
	
}

package fr.uge.bigadventure.analyser;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import fr.uge.bigadventure.element.gridelement.GridElement;

public class Parser {
	private final ArrayList<Result> tokenList;	
	
	public Parser(ArrayList<Result> tokenList) {
		Objects.requireNonNull(tokenList);
		this.tokenList = tokenList;
	}

	/** Returns a String for a single line of a .map file for it to be examined with a regex
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A String to be analysed by sub-methods of parseMap
	 */
	public String iteratorToString(ListIterator<Result> iterator) {	
		Pattern breakingPattern = Pattern.compile(".*\\)(\\[|[a-z])+"); // any character then ) then [ or a lowercase character
		var tmp = iterator.next();
		var sizeBuilder = new StringBuilder(tmp.content());
		var tmp_l = 0;
		var m = breakingPattern.matcher(sizeBuilder);
		while(!m.matches()) {
			tmp = iterator.next();
			sizeBuilder.append(tmp.content());
			tmp_l = tmp.content().length();
			m = breakingPattern.matcher(sizeBuilder);
		}
		tmp = iterator.previous();
		sizeBuilder.delete(sizeBuilder.length() - tmp_l, sizeBuilder.length()); // Supprime le dernier lexeme qu'on voulait identifier mais qu'on veut pas dans le String 
		return sizeBuilder.toString();
	}
	
	/** The general method to parse a .map file
	 * It calls sub-methods depending of what the iterator on the file finds.
	 * 
	 */
	public void parseMap() {
		var tokenIterator = tokenList.listIterator();
		while(tokenIterator.hasNext()) {
			var token = tokenIterator.next();
			switch(token.token()) {
				case IDENTIFIER -> parseIdentifier(tokenIterator);
				case LEFT_BRACKET -> {}
				default -> throw new IllegalArgumentException("Unexpected value: " + token.token());
			}
			
		}
	}
	
	/** Redirects the parsing to the dedicated parsing method depending of the identifier
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 */
	private void parseIdentifier(ListIterator<Result> iterator) {
		Objects.requireNonNull(iterator);
		Point size = null; HashMap<Character,String> encodings = null;
		switch(iterator.previous().content()) {
			case "size" -> size = parseMapSize(iterator);
			case "encodings" -> encodings = parseMapEncodings(iterator);
			case "grid" -> parseBrackets(iterator);
			case "data" -> parseMapData(iterator, size, encodings);
		}
	}

	/** Parses the [grid] or [element] line in a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 */
	private void parseBrackets(ListIterator<Result> iterator) {
		iterator.previous(); // to get again the left bracket
		Objects.requireNonNull(iterator);
		Pattern bracketPattern = Pattern.compile("\\[[a-z]+\\]");
		var bracketString = iterator.next().content() + iterator.next().content() + iterator.next().content();
		System.out.println(bracketString);
		var m = bracketPattern.matcher(bracketString);
		if(!m.matches()) {
			throw new IllegalArgumentException("Grid string does not match with regex");
		}
	}
	
	/** Parses the main section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A point with width of the grid as x and height of the grid as y
	 */
	private Point parseMapSize(ListIterator<Result> iterator) {
		Objects.requireNonNull(iterator);
		Pattern sizePattern = Pattern.compile("size:\\((\\d)x(\\d)\\)");
		var sizeString = iteratorToString(iterator);
		var m = sizePattern.matcher(sizeString);
		if(!m.matches()) {
			throw new IllegalArgumentException("Size string does not match with regex");
		}
		int width = Integer.parseInt(m.group(1)); int height = Integer.parseInt(m.group(2));
		return new Point(width, height);	
	}
	
	/** Parses the encodings section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A map associating a character with a skin
	 */
	private HashMap<Character, String> parseMapEncodings(ListIterator<Result> iterator) {	
		var encodingsMap = new HashMap<Character, String>();
		Objects.requireNonNull(iterator);		
		var encodingString = iterator.next().content() + iterator.next().content(); // "encodings:"
		if(!encodingString.equals("encodings:")) {
				throw new IllegalArgumentException("Encoding string does not match with regex");
		}
		var encodingPattern = Pattern.compile("([A-Z]+)\\(([A-Z])\\)");
		encodingString += iteratorToString(iterator); // ajoute WALL(W), BRICK(B), etc.
		var m = encodingPattern.matcher(encodingString);
		while(m.find()) {
			var skin = GridElement.checkSkinFile(m.group(1).toLowerCase(Locale.ROOT) + ".png");
			if(encodingsMap.putIfAbsent(m.group(2).charAt(0), skin) != null) {
				throw new IllegalStateException(m.group(2) + " symbol is already defined as " + m.group(1));
			}
		}
		if(encodingsMap.isEmpty()) {
			throw new IllegalStateException("The encodings map should not be empty");
		}
		return encodingsMap;	
	}
	
	/** Parses the data section of a .map file
	 * 
	 * @param iterator The iterator corresponding to the .map file
	 * @return A 2D Array of Obstacle and Decoration
	 */
	private GridElement[][] parseMapData(ListIterator<Result> iterator, Point size, HashMap<Character, String> encodings) {
		Objects.requireNonNull(iterator);
		Objects.requireNonNull(size);
		Objects.requireNonNull(encodings);
		
		var grid = new GridElement[size.x][size.y];
		return grid;
	}
	
	public static void main(String[] args) throws IOException {
    var path = Path.of("maps/big.map");
    var parser = new Parser(Lexer.toList(path));
    parser.parseMap();
	}
	
}

package fr.uge.bigadventure.analyser;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.HashMap;
>>>>>>> stash
import java.util.ListIterator;
<<<<<<< HEAD
=======
import java.util.Locale;
>>>>>>> stash
import java.util.Objects;
import java.util.regex.Pattern;

public class Parser {
	private final ArrayList<Result> tokenList;	
	
	public Parser(ArrayList<Result> tokenList) {
		Objects.requireNonNull(tokenList);
		this.tokenList = tokenList;
	}

	public void parseMap() {
		var tokenIterator = tokenList.listIterator();
		while(tokenIterator.hasNext()) {
			var token = tokenIterator.next();
			switch(token.token()) {
				case IDENTIFIER -> parseIdentifier(tokenIterator);
				default -> throw new IllegalArgumentException("Unexpected value: " + token.token());
			}
			
		}
	}

	private void parseIdentifier(ListIterator<Result> iterator) {
		Objects.requireNonNull(iterator);
//		Point size; HashMap<String,String> encodings;
		switch(iterator.previous().content()) {
			case "size" -> System.out.println(parseMapSize(iterator));
			case "encodings" -> parseMapEncodings(iterator);
//			case "data" -> parseMapData(iterator, size, encodings);
		}
	}

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
	
	/** Parse la section encodings du fichier .map
	 * 
	 * @param iterator L'itérateur du Lexer du fichier .map
	 * @return Un dictionnaire associant une image d'Obstacle à un symbole
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
			if(encodingsMap.putIfAbsent(m.group(2).charAt(0), "obstacle/" + m.group(1).toLowerCase(Locale.ROOT)) != null) {
				throw new IllegalStateException("This symbol is already defined as " + m.group(1));
			}
		}
		System.out.println(encodingsMap);
		return encodingsMap;	
	}
	
	/** Parse la section data du fichier .map
	 * 
	 * @param iterator L'itérateur du Lexer du fichier .map
	 * @return Un double tableau d'obstacles
	 */
	private Obstacle[][] parseMapData(ListIterator<Result> iterator, Point size, HashMap<String, String> encodings) {
		Objects.requireNonNull(iterator);
		
		
	}
	
	public static void main(String[] args) throws IOException {
    var path = Path.of("maps/badGridDataEncodingDefinedTwice.map");
    var parser = new Parser(Lexer.toList(path));
    parser.parseMap();
	}
	
}

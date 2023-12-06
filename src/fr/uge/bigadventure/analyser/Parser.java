package fr.uge.bigadventure.analyser;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ListIterator;
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
		switch(iterator.previous().content()) {
			case "size" -> System.out.println(parseMapSize(iterator));
			case "encodings" -> parseMapEncodings(iterator);
			case "data" -> parseMapData(iterator);
		}
	}

	public String iteratorToString(ListIterator<Result> iterator) {	
		var sizeBuilder = new StringBuilder();
		var tmp = iterator.next();
		while(tmp.token() != Token.RIGHT_PARENS) {
			sizeBuilder.append(tmp.content());
			tmp = iterator.next();
		}
		sizeBuilder.append(")");
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
	/*private Map<String,String> parseMapEncodings(ListIterator<Result> iterator) {
		Objects.requireNonNull(iterator);
		String encoding = "([A-Z]+)\\(([A-Z])\\)";
//		Pattern encodingPattern = Pattern.compile("encodings:" + encoding + "+");
//		var encodingString = iterator.next().content() + iterator.next().content(); // "encodings:"
		String encodingString = "";
		var i = 0;
		while(i < 3) {
			encodingString += iteratorToString(iterator); // ajouter WALL(W), BRICK(B), etc.
			i++;
		}
		System.out.println(encodingString);
		var m = encodingPattern.matcher(encodingString);
		if(!m.matches()) {
			throw new IllegalArgumentException("Encoding string does not match with regex");
		}
		while(m.find()) {
			System.out.println(m.group(1));
			System.out.println(m.group(2));
		}
		
		
	}*/
	
	/** Parse la section data du fichier .map
	 * 
	 * @param iterator L'itérateur du Lexer du fichier .map
	 * @return Un double tableau d'obstacles
	 */
	/*private Obstacle[][] parseMapData(ListIterator<Result> iterator) {
		Objects.requireNonNull(iterator);
		
		
	}*/
	
	public static void main(String[] args) throws IOException {
    var path = Path.of("maps/big.map");
    var parser = new Parser(Lexer.toList(path));
    parser.parseMap();
	}
	
}

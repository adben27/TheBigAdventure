package fr.uge.bigadventure.analyser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import fr.uge.bigadventure.GameMap;

public class Parser {
	private final ArrayList<Result> tokenList;
	
	public Parser(ArrayList<Result> tokenList) {
		Objects.requireNonNull(tokenList);
		this.tokenList = tokenList;
	}

	public GameMap parseMap() {
		var iterator = tokenList.iterator();
		parseMapSize(iterator);
	}
	
	public void parseMapSize(Iterator<Result> iterator) {
		Objects.requireNonNull(iterator);
		while(iterator.hasNext()) {
			var token = iterator.next().token();
			switch(token) {
				
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
    var path = Path.of("maps/big.map");
    var parser = new Parser(Lexer.toList(path));
    System.out.println(parser.tokenList);
	}
	
}

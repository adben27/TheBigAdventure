package fr.uge.bigadventure;

import java.util.HashMap;

public record Grid(int width, int height, HashMap<Element, Character> encodings, char[][] grid) {
	
	public Grid {
		if(width == 0 || height == 0) {
			throw new IllegalArgumentException("La largeur et longueur ne doivent pas être égal à zéro");
		}
		encodings = new HashMap<>();
		grid = new char[height][width];	
	}
}

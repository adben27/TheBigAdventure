package fr.uge.bigadventure.element;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public sealed interface GridElement extends Element permits Decoration, Obstacle {
//	boolean isWalkable();

	String skin();
	Point position();
	
	static String checkSkinFile(String skin) {
		String root = "src/fr/uge/bigadventure/graphic/";
		if(!Files.exists(Path.of(root + "img/obstacle/" + skin	))) {
			if(!Files.exists(Path.of(root + "img/scenery/" + skin	))) {
				throw new NoSuchElementException(skin + " is not an Obstacle neither a Decoration");
			}
			return "img/scenery/" + skin;
		}
		return "img/obstacle/" + skin;
	}
}

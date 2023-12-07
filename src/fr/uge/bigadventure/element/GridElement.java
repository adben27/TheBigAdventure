package fr.uge.bigadventure.element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public sealed interface GridElement permits Decoration, Obstacle {
	boolean isWalkable();
	
	static String checkSkinFile(String skin) {
		String root = "src/fr/uge/bigadventure/graphic/img/";
		if(!Files.exists(Path.of(root + "obstacle/" + skin	))) {
			if(!Files.exists(Path.of(root + "scenery/" + skin	))) {
				throw new NoSuchElementException(skin + " is not an Obstacle neither a Decoration");
			}
			return root + "scenery/" + skin;
		}
		return root + "obstacle/" + skin;
	}
}
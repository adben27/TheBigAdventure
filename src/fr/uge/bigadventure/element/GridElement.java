package fr.uge.bigadventure.element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public sealed interface GridElement permits Decoration, Obstacle {
	boolean isWalkable();
	
	static String checkSkinFile(String skin) {
		if(!Files.exists(Path.of("src/fr/uge/bigadventure/graphic/img/obstacle/" + skin	))) {
			if(!Files.exists(Path.of("src/fr/uge/bigadventure/graphic/img/scenery/" + skin	))) {
				throw new NoSuchElementException(skin + " is not an Obstacle neither a Decoration");
			}
			return "src/fr/uge/bigadventure/graphic/img/scenery/" + skin;
		}
		return "src/fr/uge/bigadventure/graphic/img/obstacle/" + skin;
	}
}

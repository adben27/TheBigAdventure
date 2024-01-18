package fr.uge.bigadventure.element;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.uge.bigadventure.analyser.Parser;
import fr.uge.bigadventure.analyser.Result;

public sealed interface GridElement permits Decoration, Obstacle {
	
	String skin();
	Point position();
	/** Checks if an entity can walk on this GridElement
	 * 
	 * @return true if the entity can walk on this GridElement, else false
	 */
	default boolean isWalkable() {
		return true;
	};
	
	/** Searches in the file system if this skin exists as an Obstacle or a Decoration
	 * 
	 * @param result The last result used to date for line number in case of error
	 * @param skin The name of the skin
	 * @return The partial skin path of the skin
	 */
	static String checkSkinFile(Result result, String skin) {
		String root = "src/fr/uge/bigadventure/graphic/";
		if(!Files.exists(Path.of(root + "img/obstacle/" + skin	+ ".png" ))) {
			if(!Files.exists(Path.of(root + "img/scenery/" + skin	+ ".png" ))) {
				System.err.println(Parser.lineError(result, skin + " is not an Obstacle neither a Decoration"));
			}
			return "scenery/" + skin;
		}
		return "obstacle/" + skin;
	}
}

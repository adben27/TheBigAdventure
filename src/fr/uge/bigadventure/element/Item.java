package fr.uge.bigadventure.element;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Path;

import fr.uge.bigadventure.analyser.Parser;
import fr.uge.bigadventure.analyser.Result;

public sealed interface Item extends Element permits InventoryItem, Weapon, Food {
	String skin();
	Point position();
	
	
	 /** Searches in the file system if this skin exists as an Item or Food
	 * 
	 * @param result The last result used to date for line number in case of error
	 * @param skin The name of the skin
	 * @return The partial skin path of the skin
	 */
	static String checkSkinFile(Result result, String skin) {
		String root = "src/fr/uge/bigadventure/graphic/";
		if(!Files.exists(Path.of(root + "img/item/" + skin	+ ".png" ))) {
			if(!Files.exists(Path.of(root + "img/food/" + skin	+ ".png" ))) {
				System.err.println(Parser.lineError(result, skin + " is not an Item neither Food"));
			}
			return "food/" + skin;
		}
		return "item/" + skin;
	}
}

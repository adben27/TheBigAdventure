package fr.uge.bigadventure.element;

import java.awt.image.BufferedImage;

import fr.uge.bigadventure.element.entity.Entity;
import fr.uge.bigadventure.element.gridelement.GridElement;

public sealed interface Element permits GridElement, Entity {

	/* La méthode skinToBufferedImage renvoie un BufferedImage pour un skin donné
	 * Ce skin est alors ajouté dans une map qui associe skin et bufferedimage
	 * on 
	 * 
	 */
	
	
//	public static BufferedImage skinToBufferedImage(String skin) {
//		// TODO
//	}
}

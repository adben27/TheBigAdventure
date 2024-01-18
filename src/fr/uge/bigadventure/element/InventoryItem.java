package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record InventoryItem(String skin, Point position) implements Item {

	/** Creates an inventory item
	 * 
	 * @param skin The partial skin path of the inventory item
	 * @param position The position of the inventory item in the grid
	 */
	public InventoryItem {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

}

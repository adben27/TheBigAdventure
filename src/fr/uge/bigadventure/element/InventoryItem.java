package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record InventoryItem(String skin, Point position) implements Item {
	
	public InventoryItem {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

}

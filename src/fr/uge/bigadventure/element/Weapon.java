package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Weapon(String name, String skin, int damage, Point position) implements Item {
	
	/** Creates a weapon item
	 * 
	 * @param name The name of the weapon
	 * @param skin The partial skin path of the weapon
	 * @param position The position of the weapon in the grid
	 */
	public Weapon {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(damage <= 0) {
			throw new IllegalArgumentException("damage can't be zero or less");
		}
	}

}

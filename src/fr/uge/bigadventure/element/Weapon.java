package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

public record Weapon(String name, String skin, int damage, Point position) implements Item {
	
	public static ArrayList<Weapon> weaponList = new ArrayList<>();
	
	public Weapon {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(damage <= 0) {
			throw new IllegalArgumentException("damage can't be zero or less");
		}
	}

}

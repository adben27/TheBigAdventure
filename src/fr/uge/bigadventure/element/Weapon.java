package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Weapon(String name, String skin, Point position, int damage) implements Element {
	
	public Weapon {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}
}

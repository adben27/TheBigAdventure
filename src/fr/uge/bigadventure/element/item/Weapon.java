package fr.uge.bigadventure.element.item;

import java.awt.Point;
import java.util.Objects;

import fr.uge.bigadventure.element.Element;

public record Weapon(String name, String skin, Point position, int damage) implements Element {
	
	public Weapon {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}
}

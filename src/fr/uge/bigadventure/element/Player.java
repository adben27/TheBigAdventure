package fr.uge.bigadventure.element;

import java.awt.Point;
import java.nio.file.Path;
import java.util.Objects;

public class Player implements Element {
	private final String name;
	private final Path skin;
	public final int health;
	public final Point position;
	
	public Player(String name, String skin, int health, Point position) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		this.name = name;
		this.skin = Path.of(skin);
		this.health = health;
		this.position = position;
	}
	
}

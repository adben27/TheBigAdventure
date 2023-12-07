package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public class Player implements Element {
	private final String name;
	private final String skin;
	public int health;
	public final Point position;
	
	public Player(String name, String skin, int health, Point position) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		this.name = name;
		this.skin = skin;
		this.health = health;
		this.position = position;
	}
	
}

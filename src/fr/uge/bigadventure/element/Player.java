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
		if(health <= 0) {
			throw new IllegalArgumentException("health or damage can't be zero or less");
		}	
		this.name = name;
		this.skin = skin;
		this.health = health;
		this.position = position;
	}
	
}

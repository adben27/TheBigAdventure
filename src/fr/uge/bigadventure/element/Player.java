package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public class Player implements Element {
	private final String name;
	private final String skin;
	private int health;
	private final Point position;
	
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

	public String name() {
		return name;
	}
	
	public String skin() {
		return skin;
	}
	
	public int health() {
		return health;
	}
	
	public Point position() {
		return position;
	}
	
	public void reduceHealth(int damage) {
		if(damage > health) {
			throw new IllegalArgumentException("Damage must be less than the remaining health of the player");
		}
		health -= damage;
	}
	
}

package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public class Enemy implements Element {
	private final String name;
	private final String skin;
	public int health;
	public final Point position;
	private final int damage;
	// private final String behavior; Pour l'instant on a un comportement stroll
	
	public Enemy(String name, String skin, int health, Point position, int damage) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(health <= 0 || damage <= 0) {
			throw new IllegalArgumentException("health or damage can't be zero or less");
		}
		this.name = name;
		this.skin = skin;
		this.health = health;
		this.position = position;
		this.damage = damage;
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
	
	public int damage() {
		return damage;
	}
	

	
	
}
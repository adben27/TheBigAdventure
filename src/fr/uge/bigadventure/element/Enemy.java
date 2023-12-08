package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public final class Enemy implements Entity  {
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
	
	@Override
	public String skin() {
		return skin;
	}
	
	@Override
	public int health() {
		return health;
	}

	@Override
	public Point position() {
		return position;
	}
	
	public int damage() {
		return damage;
	}

	@Override
	public boolean reduceHealth(int damage) {
		health -= damage;
		if (health <= 0) {return true;}
		return false;
	}
	
}

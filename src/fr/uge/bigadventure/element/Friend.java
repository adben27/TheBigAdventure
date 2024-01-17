package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public final class Friend implements Entity  {
	private final String name;
	private final String skin;
	public int health;
	private final int initialHealth;
	public final Point position;
	
	public Friend(String name, String skin, int health, Point position) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(health <= 0) {
			throw new IllegalArgumentException("health can't be zero or less");
		}
		this.name = name;
		this.skin = skin;
		this.health = health;
		this.initialHealth = health;
		this.position = position;
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
	public int initialHealth() {
		return initialHealth;
	}
	
	@Override
	public Point position() {
		return position;
	}
	
	@Override
	public boolean reduceHealth(int damage) {
		health -= damage;
		if (health <= 0) {return true;}
		return false;
	}
	
}

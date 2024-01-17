package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.List;
import java.util.Objects;

public final class Enemy implements Entity  {
	private final String name;
	private final String skin;
	public int health;
	private final int initialHealth;
	public final Point position;
	List<Point> zone;
	private final int damage;
	private final Behavior behavior;
	
	public Enemy(String name, String skin, int health, Point position, List<Point> zone, int damage, Behavior behavior) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		Objects.requireNonNull(zone);
		Objects.requireNonNull(behavior);	
		if(health <= 0 || damage <= 0) {
			throw new IllegalArgumentException("health or damage can't be zero or less");
		}
		this.name = name;
		this.skin = skin;
		this.health = health;
		this.initialHealth = health;
		this.position = position;
		this.zone = zone;
		this.damage = damage;
		this.behavior = behavior;
	}

	@Override
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
	
	public int damage() {
		return damage;
	}

	public Behavior behavior() {
		return behavior;
	}
	
	@Override
	public boolean reduceHealth(int damage) {
		health -= damage;
		if (health <= 0) {return true;}
		return false;
	}

	@Override
	public String toString() {
		return "Enemy : " + name + " " + skin + " health " + health + " " + position + " " + zone + "damage " + damage;  
	}
}

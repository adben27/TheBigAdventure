package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public final class Player implements Entity {
	private final String name;
	private final String skin;
	private int health;
	private final int initialHealth;
	private final Point position;

	/** Creates a Player
	 * 
	 * @param name The name of the player
	 * @param skin The partial skin path of the player
	 * @param health The health of the player
	 * @param position The position of the player
	 */
	public Player(String name, String skin, int health, Point position) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(health <= 0) {
			throw new IllegalArgumentException("health or damage can't be zero or less");
		}	
		this.name = name;
		this.skin = skin;
		this.initialHealth = health;
		this.health = health;
		this.position = position;
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
	public int initialHealth() {
		return initialHealth;
	}
	
	@Override
	public int health() {
		return health;
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

	@Override
	public String toString() {
		return "Player : " + name + " " + skin + " health " + health + " " + position;  
	}
	
}

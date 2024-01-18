package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.List;
import java.util.Objects;

public final class Enemy implements Entity  {
	private final String name;
	private final String skin;
	private int health;
	private final int initialHealth;
	public final Point position;
	List<Point> zone;
	private final int damage;
	private final Behavior behavior;

	/** Creates an Enemy
	 * 
	 * @param name The name of the enemy
	 * @param skin The partial skin path of the enemy
	 * @param health The health of the enemy
	 * @param position The initial position of the enemy
	 * @param zone The zone in which the enemy can operate (is not implemented at the moment)
	 * @param damage The damage that the enemy will cause
	 * @param behavior The behavior of the enemy (is not used, always stroll)
	 */
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

	/** Returns the damage of this enemy
	 * 
	 * @return damage that this enemy will cause
	 */
	public int damage() {
		return damage;
	}

	/** Returns the behavior of this enemy
	 *  At the moment, the behavior does not matter. The enemies are always stroll.
	 *  
	 * @return behavior the behavior of this enemy
	 */
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

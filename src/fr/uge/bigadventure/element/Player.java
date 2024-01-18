package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Player implements Entity {
	private final String name;
	private final String skin;
	private int health;
	private final int initialHealth;
	private final Point position;
	private Weapon currentWeapon;
	private ArrayList<Element> inventory;

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
		this.inventory = new ArrayList<Element>();
	}
	
	public static void loot(Player baba, List<Weapon> weaponList) {
		Objects.requireNonNull(baba);
		Objects.requireNonNull(weaponList);
		for (var weapon : weaponList) {
			if (baba.position().x == weapon.position().x && baba.position().y == weapon.position().y) {
				baba.currentWeapon = weapon;
				break;
			}
		}
		weaponList.remove(baba.currentWeapon);
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

	public Weapon currentWeapon() {
		return currentWeapon;
	}
	
	public ArrayList<Element> inventory() {
		return inventory;
	}
	
	@Override
	public boolean reduceHealth(int damage) {
		health -= damage;
		if (health <= 0) {return true;}
		return false;
	}

	/** Increases the health of the Player by bonus amount.
	 *	Would have been used for food, but we did not have enough time to implement food
	 *  Every item has 5 as default bonus, as there is no instruction in the .map file
	 *  for the amount of bonus of an item
	 * 
	 * @param bonus the amount in which the player health will be increased
	 */
	public void increaseHealth(InventoryItem item) {
		Objects.requireNonNull(item);
		health += item.bonus();
		inventory.remove(item);
	}
	
	@Override
	public String toString() {
		return "Player : " + name + " " + skin + " health " + health + " " + position;  
	}
	
}

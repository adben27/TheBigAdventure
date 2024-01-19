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
	private ArrayList<Item> inventory;

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
		this.inventory = new ArrayList<Item>();
	}
	
	 /** Get an item on the ground and add it to the inventory
	  * 
	  * @param baba The player
	  * @param weaponList List of weapons on the map
	  * @param invItemList List of items on the map
	  */
	public static void loot(Player baba, List<Weapon> weaponList, List<Item> invItemList) {
		Objects.requireNonNull(baba);
		Objects.requireNonNull(weaponList);
		Objects.requireNonNull(invItemList);
		for(var weapon : weaponList) {			
			baba.currentWeapon = weapon;
			baba.inventory.add(weapon);
			weaponList.remove(baba.currentWeapon);
			return;
		}
		for (var item : invItemList) {
			if (baba.position().x == item.position().x && baba.position().y == item.position().y && baba.inventory.size() < 36) {
				switch(item) {
					case Weapon weapon -> {} // cas géré au-dessus dans la liste des weapon 
					case InventoryItem invItem -> { baba.inventory.add(invItem); invItemList.remove(invItem); return; }
					case Food food -> { baba.inventory.add(food); invItemList.remove(food); return; }
				};
			}
		}
	}
	
	/** Use the selected item
	 * 
	 * @param baba The player
	 * @param index Index of the selected item
	 */
	public static void useItem(Player baba, int index) {
		Objects.requireNonNull(baba);
		if (index >= baba.inventory.size()){return;}
		switch(baba.inventory.get(index)) {
			case Weapon weapon -> baba.currentWeapon = weapon;
			case InventoryItem invItem -> {}
			case Food food -> baba.increaseHealth(food);
		}
	}
	
	/** Delete the selected item
	 * 
	 * @param itemList List of items in the inventory
	 * @param index Index of the selected item
	 */
	public static void deleteItem(List<Item> itemList, int index) {
		Objects.requireNonNull(itemList);
		if (index >= itemList.size()){return;}
		itemList.remove(index);
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
	
	public ArrayList<Item> inventory() {
		return inventory;
	}
	
	@Override
	public boolean reduceHealth(int damage) {
		health -= damage;
		if (health <= 0) {return true;}
		return false;
	}

	/** Increases the health of the Player by bonus amount.
	 *  Every food has 2 as default bonus, as there is no instruction in the .map file
	 * 
	 * @param food The food that the player eats
	 */
	public void increaseHealth(Food food) {
		Objects.requireNonNull(food);
		health += food.bonus();
		inventory.remove(food);
	}
	
	@Override
	public String toString() {
		return "Player : " + name + " " + skin + " health " + health + " " + position;  
	}
	
}

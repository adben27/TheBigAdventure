package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public sealed interface Entity extends Element permits Player, Enemy, Friend {

	/** Returns the name of the entity
	 * 
	 * @return the name of the entity
	 */
	String name();
	
	/** Returns the name of the entity
	 * 
	 * @return the name of the entity
	 */
	Point position();
	
	/** Returns the name of the entity
	 * 
	 * @return the name of the entity
	 */
	String skin();
	
	/** Returns the name of the entity
	 * 
	 * @return the name of the entity
	 */
	int health();
	
	/** Returns the name of the entity
	 * 
	 * @return the name of the entity
	 */
	int initialHealth();
	
	/** Reduce the health by damage
	 * 
	 * @param damage the damage that the health of the entity will take
	 * @return true if the health is zero or null, false else
	 */
	boolean reduceHealth(int damage);

	/** Moves an entity in the grid
	 * 
	 * @param entity the entity to be moved
	 * @param moveX the new X coordinate of the entity 
	 * @param moveY the new Y coordinate of the entity
	 */
	public static void entityMove(Entity entity, int moveX, int moveY) { // Prend un Player et le déplace
		Objects.requireNonNull(entity);
    entity.position().x += moveX;
    entity.position().y += moveY;
	}
	
}

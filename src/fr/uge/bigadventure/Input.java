package fr.uge.bigadventure;

import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.GridElement;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.KeyboardKey;

public class Input {
	
	/** Do an action on an Entity depending on the key pressed
	 * 
	 * @param key The key pressed by the user
	 * @param grid The map of the game
	 * @param entity The Entity who have to do an action
	 * 
	 * @return A Point according to the direction key pressed, or null if the KeyboardKey is not à direction
	 */
	public static Point keySwitch(KeyboardKey key, GridElement[][] grid, Entity entity) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entity);
		int x = 0;
		int y = 0;
		switch (key) {
  		case UP: {if (GridElement.isWalkable(grid[entity.position().x][entity.position().y-1])) {y--;}
  			break;
  		}
  		case DOWN: {if (GridElement.isWalkable(grid[entity.position().x][entity.position().y+1])) {y++;}
  			break;
  		}
  		case LEFT: {if (GridElement.isWalkable(grid[entity.position().x-1][entity.position().y])) {x--;}
  			break;
  		}
  		case RIGHT: {if (GridElement.isWalkable(grid[entity.position().x+1][entity.position().y])) {x++;}
  			break;
  		}
  		default:
  			return null;
  	}
		Entity.entityMove(entity, x, y);
		return new Point(x,y);
	}

	/** Retain a direction
	 * 
	 * @param key A direction key
	 * @return A Point according to the direction key pressed 
	 */
	public static Point keySwitch(KeyboardKey key) {
		Objects.requireNonNull(key);
		int x = 0;
		int y = 0;
		switch (key) {
  		case UP: {y--;
  			break;
  		}
  		case DOWN: {y++;
  			break;
  		}
  		case LEFT: {x--;
  			break;
  		}
  		case RIGHT: {x++;
  			break;
  		}
  		default:
  			break;
  	}
		return new Point(x,y);
	}
	
	/** Choose a random direction key
	 * 
	 * @return The KeybordKey of the direction 
	 */
	public static KeyboardKey randomKey(){
    Random r = new Random();
   	var n = r.nextInt(4);
		return switch (n) {
  		case 0 -> KeyboardKey.UP;
  		case 1 -> KeyboardKey.DOWN;
  		case 2 -> KeyboardKey.LEFT;
  		case 3 -> KeyboardKey.RIGHT;
		default -> throw new IllegalArgumentException("Unexpected value: " + n);
  	};
	}
	
	/** Reduce the health of enemies in the range of the player attack
	 * 
	 * @param lastMove A Point in the direction of the last move
	 * @param baba The player
	 * @param enemyList List of enemies in the game
	 * @param entityList List of entity in the game
	 */
	public static void hit(Point lastMove, Player baba, List<Enemy> enemyList, List<Entity> entityList) {
		Objects.requireNonNull(lastMove);
		Objects.requireNonNull(baba);
		Objects.requireNonNull(enemyList);
		var hit1 = new Point(baba.position().x + lastMove.x, baba.position().y + lastMove.y);
		var hit2 = new Point(baba.position().x + lastMove.x*2, baba.position().y + lastMove.y*2);
		for (var enemy : enemyList) {
			if (enemy.position().x == hit1.x && enemy.position().y == hit1.y || enemy.position().x == hit2.x && enemy.position().y == hit2.y) {
				if(enemy.reduceHealth(baba.currentWeapon().damage())){
					enemyList.remove(enemy);
					entityList.remove(enemy);
					return;
				}
			}
		}
	}
}

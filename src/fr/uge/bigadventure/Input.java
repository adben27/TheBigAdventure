package fr.uge.bigadventure;

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
	 */
	public static KeyboardKey keySwitch(KeyboardKey key, GridElement[][] grid, Entity entity) {
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
		return key;
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
	
	public static void hit(KeyboardKey lastMove, Player baba, List<Enemy> weaponList) {
		switch (lastMove) {
		case UP: {
			break;
		}
		case DOWN: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		default:
			break;
	}
		System.out.println("YES");
	}
}

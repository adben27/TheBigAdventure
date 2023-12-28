package fr.uge.bigadventure;

import java.util.Objects;
import java.util.Random;

import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.GridElement;
import fr.umlv.zen5.KeyboardKey;

public class Input {
	
  /** Test if a tile is walkable
	 * 
	 * @param tile A tile in the map
	 * @return true if is walkable, and false if not
	 */
	public static boolean isWalkable(GridElement tile) {
		if (tile == null) {return true;}
		return !tile.skin().startsWith("obstacle/");
	}
	
	public static void keySwitch(KeyboardKey key, GridElement[][] grid, Entity entity) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entity);
		int x = 0;
		int y = 0;
		switch (key) {
  		case UP: {if (isWalkable(grid[entity.position().x][entity.position().y-1])) {y--;}
  			break;
  		}
  		case DOWN: {if (isWalkable(grid[entity.position().x][entity.position().y+1])) {y++;}
  			break;
  		}
  		case LEFT: {if (isWalkable(grid[entity.position().x-1][entity.position().y])) {x--;}
  			break;
  		}
  		case RIGHT: {if (isWalkable(grid[entity.position().x+1][entity.position().y])) {x++;}
  			break;
  		}
  		default:
  			break;
  	}
		Entity.entityMove(entity, x, y);
	}
	
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
}

package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.Obstacle;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
	public static HashMap<String, BufferedImage> skinMap = new HashMap<>();;
	
	public static final int imgSize = 24; //La taille des images du projet
	
	public static float width;  // Largeur de l'écran en pixel
	public static float height; // Hauteur de l'écran en pixel
	
	public static int nbCasesX; // Largeur de l'écran en nombre de cases de 24px
	public static int nbCasesY; // Hauteur de l'écran en nombre de cases de 24px
	
	public static int sizeX;    // Largeur de la map
	public static int sizeY;    // Hauteur de la map
	
	public Graphic(ScreenInfo screenInfo, Obstacle[][] grid) {
		Objects.requireNonNull(screenInfo);
		Objects.requireNonNull(grid);
    width = screenInfo.getWidth();
    height = screenInfo.getHeight();
    nbCasesX = (int) (width / imgSize);
    nbCasesY = (int) (height / imgSize);
    sizeX = grid.length;
    sizeY = grid[0].length;
  }
  
  public static int shiftX(int coordinate) { // Transforme une coordonnée dans la map en coordonné à l'écran
  	return (coordinate + nbCasesX/2 - sizeX/2) * imgSize;
  }
  public static int shiftY(int coordinate) { // Transforme une coordonnée dans la map en coordonné à l'écran
  	return (coordinate + nbCasesY/2 - sizeY/2) * imgSize;
  }
	
	public static boolean isWalkable(Obstacle tile) {
		if (tile == null) {return true;}
		return !tile.skin().startsWith("obstacle/");
	}
  
	public static void printCase(Obstacle tile, Graphics2D map) { // Prend un obstacle et l'affiche à sa position()
		Objects.requireNonNull(tile);
		Objects.requireNonNull(map);
    map.drawImage(skinMap.get(tile.skin()), shiftX(tile.position().x), shiftY(tile.position().y), null);
	}
	
	
	public static void printMap(Graphics2D map, Obstacle[][] grid) { // Prend un double tableau d'Obstacle (une map) et l'affiche 
		Objects.requireNonNull(grid);
		Objects.requireNonNull(map);
		map.setColor(Color.BLACK);
    map.fill(new Rectangle2D.Float(0, 0, width, height));
		for(var list : grid) {
			for(var elem : list) {
				if (elem != null) {printCase(elem, map);}
			}
		}
	}
	
	public static void entityMove(Graphics2D move, Obstacle[][] grid, Entity entity, int moveX, int moveY) { // Prend un Player et le déplace
		Objects.requireNonNull(move);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entity);
    move.setColor(Color.BLACK);
    move.fill(new Rectangle2D.Float(shiftX(entity.position().x), shiftY(entity.position().y)-4, imgSize, imgSize+4));
		if (grid[entity.position().x][entity.position().y] != null) {
	    move.drawImage(skinMap.get(grid[entity.position().x][entity.position().y].skin()), shiftX(entity.position().x), shiftY(entity.position().y), null);
		}
		if (grid[entity.position().x][entity.position().y-1] != null) {
	    move.drawImage(skinMap.get(grid[entity.position().x][entity.position().y-1].skin()), shiftX(entity.position().x), shiftY(entity.position().y-1), null);
		}
    entity.position().x += moveX;
    entity.position().y += moveY;
    if (grid[entity.position().x][entity.position().y] != null && grid[entity.position().x][entity.position().y].skin().endsWith("vine")) {
    	entity.reduceHealth(1);
    }
		move.setColor(Color.RED);
		move.fill(new Rectangle2D.Float(shiftX(entity.position().x)+2, shiftY(entity.position().y)-4, entity.health(), 4));
    move.drawImage(skinMap.get(entity.skin()), shiftX(entity.position().x), shiftY(entity.position().y), null);
	}
	
	public static void keySwitch(Graphics2D move, KeyboardKey key, Obstacle[][] grid, Player baba) {
		Objects.requireNonNull(move);
		Objects.requireNonNull(key);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(baba);
		int x = 0;
		int y = 0;
		switch (key) {
  		case UP: {if (isWalkable(grid[baba.position().x][baba.position().y-1])) {y--;}
  			break;
  		}
  		case DOWN: {if (isWalkable(grid[baba.position().x][baba.position().y+1])) {y++;}
  			break;
  		}
  		case LEFT: {if (isWalkable(grid[baba.position().x-1][baba.position().y])) {x--;}
  			break;
  		}
  		case RIGHT: {if (isWalkable(grid[baba.position().x+1][baba.position().y])) {x++;}
  			break;
  		}
  		default:
  			break;
  	}
		entityMove(move, grid, baba, x, y);
	}
	
	public static void keySwitchEnemy(Graphics2D move, int value, Obstacle[][] grid, Enemy baba){
		Objects.requireNonNull(move);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(baba);
		int x = 0;
		int y = 0;
		switch (value) {
  		case 0: {if (isWalkable(grid[baba.position().x][baba.position().y-1])) {y--;}
  			break;
  		}
  		case 1: {if (isWalkable(grid[baba.position().x][baba.position().y+1])) {y++;}
  			break;
  		}
  		case 2: {if (isWalkable(grid[baba.position().x-1][baba.position().y])) {x--;}
  			break;
  		}
  		case 3: {if (isWalkable(grid[baba.position().x+1][baba.position().y])) {x++;}
  			break;
  		}
  		default:
  			break;
  	}
		entityMove(move, grid, baba, x, y);
	}
}



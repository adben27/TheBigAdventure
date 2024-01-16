package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.GridElement;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
	public static HashMap<String, BufferedImage> skinMap = new HashMap<>();
	
	public static final int imgSize = 24; //La taille des images du projet
	
	public static float width;  // Largeur de l'écran en pixel
	public static float height; // Hauteur de l'écran en pixel
	
	public static int nbCasesX; // Largeur de l'écran en nombre de cases de 24px
	public static int nbCasesY; // Hauteur de l'écran en nombre de cases de 24px
	
	public static int sizeX;    // Largeur de la map
	public static int sizeY;    // Hauteur de la map
	
	public Graphic(ScreenInfo screenInfo, GridElement[][] grid) {
		Objects.requireNonNull(screenInfo);
		Objects.requireNonNull(grid);
    width = screenInfo.getWidth();
    height = screenInfo.getHeight();
    nbCasesX = (int) (width / imgSize);
    nbCasesY = (int) (height / imgSize);
    sizeX = grid.length;
    sizeY = grid[0].length;
  }
	
	
	/** Transform a grid coordinate on the horizontal axis to an adapted screen coordinate
	 * 
	 * @param coordinate A grid coordinate
	 * @return The adapted screen coordinate
	 */
  public static int shiftX(int coordinate) {
  	return (coordinate + nbCasesX/2 - sizeX/2) * imgSize;
  }
	/** Transform a grid coordinate on the vertical axis to an adapted screen coordinate
	 * 
	 * @param coordinate A grid coordinate
	 * @return The adapted screen coordinate
	 */
  public static int shiftY(int coordinate) {
  	return (coordinate + nbCasesY/2 - sizeY/2) * imgSize;
  }
  
	/** Print a tile if it is not null
	 * 
	 * @param tile A tile in the map
	 * @param map 
	 */
	public static void printTile(GridElement tile, Graphics2D map) { // Prend un obstacle et l'affiche à sa position()
		Objects.requireNonNull(map);
		if (tile == null) {return;}
    map.drawImage(skinMap.get(tile.skin()), shiftX(tile.position().x), shiftY(tile.position().y), null);
	}
	
	
	public static void printMap(Graphics2D map, GridElement[][] grid) { // Prend un double tableau d'Obstacle (une map) et l'affiche 
		Objects.requireNonNull(grid);
		Objects.requireNonNull(map);
		map.setColor(Color.BLACK);
    map.fill(new Rectangle2D.Float(0, 0, width, height));
		for(var list : grid) {
			for(var elem : list) {printTile(elem, map);}
		}
	}
	
	public static void eraseEntity(Graphics2D move, GridElement[][] grid, List<Entity> entityList) {
		Objects.requireNonNull(move);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entityList);
    move.setColor(Color.BLACK);
    for(var entity : entityList) {
    	move.fill(new Rectangle2D.Float(shiftX(entity.position().x), shiftY(entity.position().y)-4, imgSize, imgSize+4));
    	printTile(grid[entity.position().x][entity.position().y], move);
    	printTile(grid[entity.position().x][entity.position().y-1], move);
		}
    
	}
	
	public static void drawEntity(Graphics2D move, List<Entity> entityList) {
		Objects.requireNonNull(move);
		Objects.requireNonNull(entityList);
		for(var entity : entityList) {
			move.setColor(Color.GRAY);
			move.fill(new Rectangle2D.Float(shiftX(entity.position().x)+2, shiftY(entity.position().y)-4, 20, 4));
			move.setColor(Color.RED);
			move.fill(new Rectangle2D.Float(shiftX(entity.position().x)+2, shiftY(entity.position().y)-4, entity.health(), 4));
			move.drawImage(skinMap.get(entity.skin()), shiftX(entity.position().x), shiftY(entity.position().y), null);
		}
	}
}



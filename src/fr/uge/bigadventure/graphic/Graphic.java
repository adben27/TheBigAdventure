package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Obstacle;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
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
  	Objects.requireNonNull(coordinate);
  	return (coordinate + nbCasesX/2 - sizeX/2) * imgSize;
  }
  public static int shiftY(int coordinate) { // Transforme une coordonnée dans la map en coordonné à l'écran
  	Objects.requireNonNull(coordinate);
  	return (coordinate + nbCasesY/2 - sizeY/2) * imgSize;
  }
	
	public static boolean isWalkable(Obstacle tile) {
		if (tile == null) {return true;}
		return !tile.skin().startsWith("obstacle");
	}
  
	public static void printCase(Obstacle tile, Graphics2D map) throws IOException{ // Prend un obstacle et l'affiche à sa position
		Objects.requireNonNull(tile);
		Objects.requireNonNull(map);
		BufferedImage image;
		try(var input = Main.class.getResourceAsStream("img/" + tile.skin() + ".png")) {
  		image = ImageIO.read(input);
  	}
    map.drawImage(image, shiftX(tile.position().x), shiftY(tile.position().y), null);
	}
	
	
	public static void printMap(Graphics2D map, Obstacle[][] grid) throws IOException{ // Prend un double tableau d'Obstacle (une map) et l'affiche 
		Objects.requireNonNull(grid);
		Objects.requireNonNull(map);
		map.setColor(Color.BLACK);
    map.fill(new Rectangle2D.Float(0, 0, width, height));
		for(var list : grid) {
			for(var elem : list) {
				if (elem == null) {continue;}
		  	printCase(elem, map);
			}
		}
	}
	
	public static void playerMove(Graphics2D move, Obstacle[][] grid, Player baba, int moveX, int moveY) throws IOException { // Prend un Player et le déplace
		Objects.requireNonNull(move);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(baba);
		Objects.requireNonNull(moveX);
		Objects.requireNonNull(moveY);
		BufferedImage image;
    move.setColor(Color.BLACK);
    move.fill(new Rectangle2D.Float(shiftX(baba.position.x), shiftY(baba.position.y), imgSize, imgSize));
		if (grid[baba.position.x][baba.position.y] != null) {
			try(var input = Main.class.getResourceAsStream("img/" + grid[baba.position.x][baba.position.y].skin() + ".png")) {
	  		image = ImageIO.read(input);
	  	}
	    move.drawImage(image, shiftX(baba.position.x), shiftY(baba.position.y), null);
		}
    baba.position.x += moveX;
    baba.position.y += moveY;
		try(var input = Main.class.getResourceAsStream("img/" + "pnj/baba" + ".png")) {
  		image = ImageIO.read(input);
  	}
    move.drawImage(image, shiftX(baba.position.x), shiftY(baba.position.y), null);
	}
	
	public static void keySwitch(Graphics2D move, KeyboardKey key, Obstacle[][] grid, Player baba) throws IOException {
		Objects.requireNonNull(move);
		Objects.requireNonNull(key);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(baba);
		int x = 0;
		int y = 0;
		switch (key) {
  		case UP: {if (isWalkable(grid[baba.position.x][baba.position.y-1])) {y--;}
  			break;
  		}
  		case DOWN: {if (isWalkable(grid[baba.position.x][baba.position.y+1])) {y++;}
  			break;
  		}
  		case LEFT: {if (isWalkable(grid[baba.position.x-1][baba.position.y])) {x--;}
  			break;
  		}
  		case RIGHT: {if (isWalkable(grid[baba.position.x+1][baba.position.y])) {x++;}
  			break;
  		}
  		default:
  			break;
  	}
		playerMove(move, grid, baba, x, y);
	}
}



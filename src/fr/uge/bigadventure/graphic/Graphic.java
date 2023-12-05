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
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
	public static final int imgSize = 24;
	
	public static float width;
	public static float height;
	
	public static int nbCasesX;
	public static int nbCasesY;
	
	public static int sizeX;
	public static int sizeY;
	
	public Graphic(ScreenInfo screenInfo, int mapX, int mapY) {
		Objects.requireNonNull(screenInfo);
    Graphic.width = screenInfo.getWidth();
    Graphic.height = screenInfo.getHeight();
    Graphic.nbCasesX = (int) (Graphic.width / imgSize);
    Graphic.nbCasesY = (int) (Graphic.height / imgSize);
    Graphic.sizeX = mapX;
    Graphic.sizeY = mapY;
  }
  
  public static int graphShiftX(int coordinate) {
  	return (coordinate + nbCasesX/2 - sizeX/2) * imgSize;
  }
  public static int graphShiftY(int coordinate) {
  	return (coordinate + nbCasesY/2 - sizeY/2) * imgSize;
  }
	
	public void printCase(Obstacle tile, Graphics2D map) throws IOException{
		Objects.requireNonNull(tile);
		Objects.requireNonNull(map);
		BufferedImage image;
		try(var input = Main.class.getResourceAsStream("img/" + tile.skin() + ".png")) {
  		image = ImageIO.read(input);
  	}
    map.drawImage(image, graphShiftX(tile.position().x), graphShiftY(tile.position().y), null);
	}
	
	
	public void printMap(Graphics2D map, Obstacle[][] grid) throws IOException{
		Objects.requireNonNull(grid);
		Objects.requireNonNull(map);
		map.setColor(Color.BLACK);
    map.fill(new Rectangle2D.Float(0, 0, width, height));
		for(var list : grid) {
			for(var elem : list) {
				if (elem.skin().equals("")) {continue;}
		  	printCase(elem, map);
			}
		}
	}
	
	public void playerMove(Graphics2D move, Player baba, int moveX, int moveY) throws IOException {
		Objects.requireNonNull(move);
		Objects.requireNonNull(baba);
		Objects.requireNonNull(moveX);
		Objects.requireNonNull(moveY);
		BufferedImage image;
		try(var input = Main.class.getResourceAsStream("img/" + "pnj/baba" + ".png")) {
  		image = ImageIO.read(input);
  	}
    move.setColor(Color.BLACK);
    move.fill(new Rectangle2D.Float(Graphic.graphShiftX(baba.position.x), Graphic.graphShiftY(baba.position.y), Graphic.imgSize, Graphic.imgSize));
    baba.position.x += moveX;
    baba.position.y += moveY;
    move.drawImage(image, Graphic.graphShiftX(baba.position.x), Graphic.graphShiftY(baba.position.y), null);
	}
}

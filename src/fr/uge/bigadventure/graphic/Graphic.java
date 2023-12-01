package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Obstacle;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
	public static float width;
	public static float height;
	
	public Graphic(ScreenInfo screenInfo) {
		Objects.requireNonNull(screenInfo);
    Graphic.width = screenInfo.getWidth();
    Graphic.height = screenInfo.getHeight();
  }
	
  public static float getWidth() {
    return width;
  }
  public static float getHeight() {
    return height;
  }
	
	public void printCase(Obstacle tile, Graphics2D map) throws IOException{
		Objects.requireNonNull(tile);
		Objects.requireNonNull(map);
		BufferedImage image;
		try(var input = Demo.class.getResourceAsStream("img/" + tile.skin() + ".png")) {
  		image = ImageIO.read(input);
  	}
    map.drawImage(image, tile.position().x * 24, tile.position().y * 24, null);
	}
	
	
	public void printMap(Obstacle[][] grid, Graphics2D map) throws IOException{
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
}

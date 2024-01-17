package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.GridElement;
import fr.uge.bigadventure.element.Obstacle;
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
	
	/** Initialize class field
	 * 
	 * @param screenInfo Info of the current screen
	 * @param grid Grid of the game 
	 */
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
	
	/** Load all images from the folder img
	 * 
	 * @throws IOException
	 */
	public static void loadImage() throws IOException {
		var folderPath = Paths.get("img/");
  	Files.walk(folderPath)
  		.filter(path -> path.toString().endsWith(".png"))
    	.forEach(imagePath -> {
    		var imageName = imagePath.toString();
        BufferedImage image;
    		try(var input = Main.class.getResourceAsStream(imageName)) {
					image = ImageIO.read(input);
					imageName = imageName.substring(4, imageName.length()-4).replace('\\', '/');
					if (imageName.startsWith("obstacle/")) {
						Obstacle.obstacleSet.add(imageName);
					}
	        Graphic.skinMap.putIfAbsent(imageName, image);
				} catch (IOException e) {
					e.printStackTrace();
				}
    	});
	}
	
	/** Transform a grid coordinate on the horizontal axis to an adapted screen coordinate
	 * 
	 * @param coordinate A grid coordinate
	 * @return The adapted screen coordinate
	 */
  public static int shiftX(int coordinate) {
  	if (sizeX < nbCasesX) {
  		return (coordinate + nbCasesX/2 - sizeX/2) * imgSize;
  	}
  	return coordinate * imgSize;
  }
	/** Transform a grid coordinate on the vertical axis to an adapted screen coordinate
	 * 
	 * @param coordinate A grid coordinate
	 * @return The adapted screen coordinate
	 */
  public static int shiftY(int coordinate) {
  	if (sizeY < nbCasesY) {
  		return (coordinate + nbCasesY/2 - sizeY/2) * imgSize;
  	}
  	return coordinate * imgSize;
  }
  
	/** Print a tile if it is not null
	 * 
	 * @param tile A tile in the map
	 * @param map
	 */
	public static void printTile(GridElement tile, Graphics2D map, int x, int y) { // Prend un obstacle et l'affiche à sa position()
		Objects.requireNonNull(map);
		if (tile == null) {return;}
    map.drawImage(skinMap.get(tile.skin()), shiftX(x), shiftY(y), null);
	}
	
	/**	Print the current map
	 * 
	 * @param map 
	 * @param grid Contains the map
	 * @param baba The player 
	 */
	public static void printMap(Graphics2D map, GridElement[][] grid, Entity baba) { // Prend un double tableau d'Obstacle (une map) et l'affiche 
		Objects.requireNonNull(grid);
		Objects.requireNonNull(map);
		map.setColor(Color.BLACK);
    map.fill(new Rectangle2D.Float(0, 0, width, height));
    int xStart = Math.min(Math.max(0, baba.position().x - nbCasesX/2), Math.max(0,sizeX - nbCasesX));
    int yStart = Math.min(Math.max(0, baba.position().y - nbCasesY/2), Math.max(0,sizeY - nbCasesY));
    int x = 0;
    int y = 0;
    for(int i = xStart; i < Math.min(xStart + nbCasesX, sizeX); i++) {
    	for(int j = yStart; j < Math.min(yStart + nbCasesY, sizeY); j++) {
    		System.out.println("i : " + x + " j : " + y);
    		printTile(grid[i][j], map, x, y);
    		y++;
    	}
    	y = 0;
    	x++;
    }
	}
	
	public static boolean scrolling() {
		return sizeX > nbCasesX || sizeY > nbCasesY;
	}
	
	public static void eraseEntity(Graphics2D erase, GridElement[][] grid, List<Entity> entityList) {
		Objects.requireNonNull(erase);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entityList);
    erase.setColor(Color.BLACK);
    for(var entity : entityList) {
    	erase.fill(new Rectangle2D.Float(shiftX(entity.position().x), shiftY(entity.position().y)-4, imgSize, imgSize+4));
    	printTile(grid[entity.position().x][entity.position().y], erase, entity.position().x, entity.position().y);
    	printTile(grid[entity.position().x][entity.position().y-1], erase, entity.position().x, entity.position().y-1);
		}
    
	}
	
	public static void drawEntity(Graphics2D draw, List<Entity> entityList) {
		Objects.requireNonNull(draw);
		Objects.requireNonNull(entityList);
		for(var entity : entityList) {
			draw.setColor(Color.GRAY);
			draw.fill(new Rectangle2D.Float(shiftX(entity.position().x)+2, shiftY(entity.position().y)-4, entity.initialHealth(), 4));
			draw.setColor(Color.RED);
			draw.fill(new Rectangle2D.Float(shiftX(entity.position().x)+2, shiftY(entity.position().y)-4, entity.health(), 4));
			draw.drawImage(skinMap.get(entity.skin()), shiftX(entity.position().x), shiftY(entity.position().y), null);
			draw.setColor(Color.WHITE);
			draw.drawString(entity.name(), shiftX(entity.position().x), shiftY(entity.position().y) + 30);
		}
	}
	
	public static void drawGameOver(Graphics2D over) {
		Objects.requireNonNull(over);
		over.setColor(Color.BLACK);
		over.fill(new Rectangle2D.Float(0, 0, width, height));
		over.setColor(Color.RED);
		over.setFont(new Font("Game Over", 1, 50));
		over.drawString("GAME OVER", width/2 - 150, height/2);
	}
}



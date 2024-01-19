package fr.uge.bigadventure.graphic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.Input;
import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.Food;
import fr.uge.bigadventure.element.GridElement;
import fr.uge.bigadventure.element.InventoryItem;
import fr.uge.bigadventure.element.Item;
import fr.uge.bigadventure.element.Player;
import fr.uge.bigadventure.element.Weapon;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;
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
	 * @param map The Graphics context
	 */
	public static void printTile(GridElement tile, Graphics2D map, int x, int y) { // Prend un obstacle et l'affiche à sa position()
		Objects.requireNonNull(map);
		if (tile == null) {return;}
    map.drawImage(skinMap.get(tile.skin()), shiftX(x), shiftY(y), null);
	}
	
	/**	Print the current map
	 * 
	 * @param map The Graphics context
	 * @param grid Contains the map
	 * @param baba The player 
	 */
	public static void printMap(Graphics2D map, GridElement[][] grid, Player baba) { // Prend un double tableau d'Obstacle (une map) et l'affiche 
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
    		printTile(grid[i][j], map, x, y);
    		y++;
    	}
    	y = 0;
    	x++;
    }
	}
	
	/** Check if the map is bigger than the screen
	 * 
	 * @return true or false accordingly
	 */
	public static boolean scrolling() {
		return sizeX > nbCasesX || sizeY > nbCasesY;
	}
	
	/** Erase entities and reset the map
	 * 
	 * @param erase The Graphics context
	 * @param grid Contains the map
	 * @param entityList List of entities
	 */
	public static void eraseEntity(Graphics2D erase, GridElement[][] grid, List<Entity> entityList) {
		Objects.requireNonNull(erase);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(entityList);
    for(var entity : entityList) {
    	int shiftName = erase.getFontMetrics(erase.getFont()).stringWidth(entity.name())/2 - imgSize/2;
    	erase.clearRect(shiftX(entity.position().x), shiftY(entity.position().y)-4, imgSize, imgSize+4);
    	erase.clearRect(shiftX(entity.position().x) - shiftName, shiftY(entity.position().y+1), shiftName*2 + imgSize, 12);
    	printTile(grid[entity.position().x][entity.position().y], erase, entity.position().x, entity.position().y);
    	printTile(grid[entity.position().x][entity.position().y-1], erase, entity.position().x, entity.position().y-1);
    	for (int i = entity.position().x - shiftName/imgSize - 1; i <= entity.position().x + shiftName/imgSize + 1; i++) {
    		if (i < 0 || i >= sizeX) {continue;}
    		printTile(grid[i][entity.position().y+1], erase, i, entity.position().y+1);
    	}
		}
    
	}
	
	/** Draw all entities in a list
	 * 
	 * @param draw The Graphics context
	 * @param entityList List of entities
	 */
	public static void drawEntity(Graphics2D draw, List<Entity> entityList) {
		Objects.requireNonNull(draw);
		Objects.requireNonNull(entityList);
		for(var entity : entityList) {
			draw.setColor(Color.GRAY);
			draw.fill(new Rectangle2D.Float(shiftX(entity.position().x), shiftY(entity.position().y)-4, 24, 4));
			draw.setColor(Color.RED);
			float percentHealth = (float) entity.health()/entity.initialHealth();
			draw.fill(new Rectangle2D.Float(shiftX(entity.position().x), shiftY(entity.position().y)-4, (int)(percentHealth*24), 4));
			draw.drawImage(skinMap.get(entity.skin()), shiftX(entity.position().x), shiftY(entity.position().y), null);
			draw.setColor(Color.WHITE);
			int shiftNameX = draw.getFontMetrics(draw.getFont()).stringWidth(entity.name())/2 - imgSize/2;
			draw.drawString(entity.name(), shiftX(entity.position().x) - shiftNameX, shiftY(entity.position().y+1) + 10);
		}
	}
	
	/** Draw the Game Over screen
	 * 
	 * @param over The Graphics context
	 */
	public static void drawGameOver(Graphics2D over) {
		Objects.requireNonNull(over);
		over.setColor(Color.BLACK);
		over.fill(new Rectangle2D.Float(0, 0, width, height));
		over.setColor(Color.RED);
		over.setFont(new Font("Game Over", 1, 50));
		over.drawString("GAME OVER", width/2 - 150, height/2);
	}
	
	/** Draw all inventory items and weapons
	 * 
	 * @param item The Graphics context
	 */
	public static void drawItem(Graphics2D item, List<Weapon> weaponList, List<Item> invItemList) {
		Objects.requireNonNull(item);
		Objects.requireNonNull(weaponList);
		for (var weapon : weaponList) {
			item.drawImage(skinMap.get(weapon.skin()), shiftX(weapon.position().x), shiftY(weapon.position().y), null);
		}
		for (var listItem : invItemList) {
			item.drawImage(skinMap.get(listItem.skin()), shiftX(listItem.position().x), shiftY(listItem.position().y), null);
		}
	}
	
	/** Erase the visual effect of the last attack
	 * 
	 * @param erase The Graphics context
	 * @param grid Contains the map
	 * @param baba The player
	 * @param hit Direction of the hit
	 */
	public static void eraseHit(Graphics2D erase, GridElement[][] grid, Player baba, Point hit) {
		Objects.requireNonNull(erase);
		Objects.requireNonNull(grid);
		Objects.requireNonNull(baba);
		Objects.requireNonNull(hit);
		if (hit.x == 0) {
			erase.clearRect(shiftX(baba.position().x), shiftY(baba.position().y - 2), imgSize, 5*imgSize);
			for (int i = baba.position().y - 2; i <= baba.position().y + 2; i++) {
    		if (i < 0 || i >= sizeX) {continue;}
    		printTile(grid[baba.position().x][i], erase, baba.position().x, i);
    	}
		}
		else {
			erase.clearRect(shiftX(baba.position().x - 2), shiftY(baba.position().y), 5*imgSize, imgSize);
			for (int i = baba.position().x - 2; i <= baba.position().x + 2; i++) {
    		if (i < 0 || i >= sizeX) {continue;}
    		printTile(grid[i][baba.position().y], erase, i, baba.position().y);
    	}
		}
	}
	
	/** Draw a hit
	 * 
	 * @param hit The Graphics context
	 * @param lastMove A Point in the direction of the hit
	 * @param baba The player
	 */
	public static void drawHit(Graphics2D hit, Point lastMove, Player baba) {
		Objects.requireNonNull(hit);
		Objects.requireNonNull(lastMove);
		Objects.requireNonNull(baba);
		hit.setColor(Color.RED);
		float alpha = 0.4f;
		hit.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		hit.fill(new Rectangle2D.Float(shiftX(baba.position().x + lastMove.x), shiftY(baba.position().y + lastMove.y), imgSize, imgSize));
		hit.fill(new Rectangle2D.Float(shiftX(baba.position().x + lastMove.x*2), shiftY(baba.position().y + lastMove.y*2), imgSize, imgSize));
		hit.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		hit.drawImage(skinMap.get(baba.currentWeapon().skin()), shiftX(baba.position().x + lastMove.x), shiftY(baba.position().y + lastMove.y), null);
	}
	
	/** Draw the structure of the inventory
	 * 
	 * @param inventory The Graphics context
	 */
	public static void drawInventoryStructure(Graphics2D inventory) {
		Objects.requireNonNull(inventory);
		inventory.setColor(Color.BLUE);
		inventory.fill(new Rectangle2D.Float(5*imgSize, 5*imgSize, 15*imgSize, imgSize));
		inventory.fill(new Rectangle2D.Float(5*imgSize, 10*imgSize, 15*imgSize, imgSize));
		inventory.fill(new Rectangle2D.Float(5*imgSize, 21*imgSize, 16*imgSize, imgSize));
		inventory.fill(new Rectangle2D.Float(5*imgSize, 17*imgSize, 15*imgSize, imgSize));
		
		inventory.fill(new Rectangle2D.Float(5*imgSize, 5*imgSize, imgSize, 16*imgSize));
		inventory.fill(new Rectangle2D.Float(20*imgSize, 5*imgSize, imgSize, 16*imgSize));
		inventory.fill(new Rectangle2D.Float(12*imgSize, 5*imgSize, imgSize, 5*imgSize));
		inventory.setColor(Color.BLACK);
		inventory.fill(new Rectangle2D.Float(21*imgSize, 7*imgSize, (nbCasesX-20)*imgSize, 2*imgSize));
	}
	
	/** Draw the player part of the inventory
	 * 
	 * @param inventory The Graphics context
	 * @param baba The player
	 */
	public static void drawPlayerBloc(Graphics2D inventory, Player baba) {
		Objects.requireNonNull(inventory);
		Objects.requireNonNull(baba);
		inventory.setColor(Color.WHITE);
		inventory.setFont(new Font("Title", 1, 20));
		inventory.drawString("Player", 7*imgSize, 7*imgSize);
		inventory.drawImage(skinMap.get(baba.skin()), 10*imgSize, 6*imgSize + 5, null);
		inventory.setFont(new Font("Info", 1, 12));
		inventory.drawString("Name : " + baba.name(), 7*imgSize, 8*imgSize);
		inventory.drawString("Health : " + baba.health(), 7*imgSize, 9*imgSize);
	}
	
	/** Draw the weapon part of the inventory
	 * 
	 * @param inventory The Graphics context
	 * @param baba The player
	 */
	public static void drawWeaponBloc(Graphics2D inventory, Player baba) {
		Objects.requireNonNull(inventory);
		Objects.requireNonNull(baba);
		inventory.setColor(Color.BLACK);
		inventory.fill(new Rectangle2D.Float(13*imgSize, 7*imgSize, (nbCasesX-12)*imgSize, 2*imgSize));
		inventory.setColor(Color.WHITE);
		inventory.setFont(new Font("Title", 1, 20));
		inventory.drawString("Weapon", 14*imgSize, 7*imgSize);
		if (baba.currentWeapon() != null) {
  		inventory.drawImage(skinMap.get(baba.currentWeapon().skin()), 18*imgSize, 6*imgSize + 5, null);
  		inventory.setFont(new Font("Info", 1, 12));
  		inventory.drawString("Name : " + baba.currentWeapon().name(), 14*imgSize, 8*imgSize);
  		inventory.drawString("Damage : " + baba.currentWeapon().damage(), 14*imgSize, 9*imgSize);
		}
	}
	
	/** Draw the item part of the inventory
	 * 
	 * @param inventory The Graphics context
	 * @param itemList List of items in the inventory
	 * @param select Contains seleted item's coordinates
	 */
	public static void drawItemBloc(Graphics2D inventory, List<Item> itemList, Point select) {
		Objects.requireNonNull(inventory);
		Objects.requireNonNull(itemList);
		Objects.requireNonNull(select);
		inventory.setColor(Color.WHITE);
		inventory.setFont(new Font("Title", 1, 20));
		inventory.drawString("Inventory", 11*imgSize, 12*imgSize);
		inventory.setColor(Color.YELLOW);
		inventory.fill(new Rectangle2D.Float(select.x*imgSize, select.y*imgSize, imgSize, imgSize));
		for (int i = 0; i < itemList.size() ; i++) {
			inventory.drawImage(skinMap.get(itemList.get(i).skin()), (7+(i%12))*imgSize, (13+i/12)*imgSize, null);
		}
	}
	
	/** Draw infos of the selected item
	 * 
	 * @param inventory The Graphics context
	 * @param item The selected item
	 */
	public static void drawSelectedBloc(Graphics2D inventory, Item item) {
		Objects.requireNonNull(inventory);
		inventory.setColor(Color.WHITE);
		inventory.setFont(new Font("Title", 1, 20));
		inventory.drawString("Selected Item", 10*imgSize+10, 19*imgSize);
		inventory.setFont(new Font("Info", 1, 12));
		inventory.drawString("Name : ", 7*imgSize, 20*imgSize);
		inventory.drawString("Effect : ", 14*imgSize, 20*imgSize);
		if (item != null) {
			switch(item) {
				case Weapon weapon -> { 
			inventory.drawString(weapon.name(), 9*imgSize, 20*imgSize);
			inventory.drawString("" + weapon.damage(), 16*imgSize, 20*imgSize); }
				case InventoryItem invItem -> {	
					inventory.drawString(invItem.skin().substring(invItem.skin().lastIndexOf("/") + 1), 9*imgSize, 20*imgSize);
					inventory.drawString("Nothing !", 16*imgSize, 20*imgSize); }
				case Food food -> { 
			inventory.drawString(food.skin().substring(food.skin().lastIndexOf("/") + 1), 9*imgSize, 20*imgSize);
			inventory.drawString("heal " + food.bonus(), 16*imgSize, 20*imgSize); }
			}
		}
	}
	
	/** Open the inventory interface
	 * 
	 * @param context The ApplicationContext
	 * @param baba The player
	 */
	public static void openInventory(ApplicationContext context, Player baba){
		Objects.requireNonNull(context);
		Objects.requireNonNull(baba);
		Event event = null;
		var move =  new Point(0, 0);
		var select = new Point(7, 13);
		while(event == null || event.getKey() != KeyboardKey.Q){
			int index = (select.x - 7) + (select.y - 13) * 12;
			if (event != null) {
      	if (event.getAction() == Action.KEY_PRESSED) {
      		KeyboardKey key = event.getKey();
      		if (key == KeyboardKey.SPACE) {
      			Player.useItem(baba, index);
      		}
      		if (key == KeyboardKey.D) {
      			Player.deleteItem(baba.inventory(), index);
      		}
      		else {
      			move = Input.keySwitch(key);
      			select.x += move.x;
      			select.y += move.y;
      			if (select.x < 7) {select.x = 7;}
      			if (select.x > 18) {select.x = 18;}
      			if (select.y < 13) {select.y = 13;}
      			if (select.y > 15) {select.y = 15;}
      		}
      	}
			}
      context.renderFrame(inventory -> {
      	inventory.setColor(Color.BLACK);
    		inventory.fill(new Rectangle2D.Float(0, 0, width, height));
    		drawPlayerBloc(inventory, baba);
    		drawWeaponBloc(inventory, baba);
    		drawItemBloc(inventory, baba.inventory(), select);
    		if (index < baba.inventory().size()){drawSelectedBloc(inventory, baba.inventory().get(index));}
    		else 												{drawSelectedBloc(inventory, null);}
    		drawInventoryStructure(inventory);
      });
      event = context.pollOrWaitEvent(10000);
		}
		context.renderFrame(inventory -> {inventory.fill(new Rectangle2D.Float(0, 0, width, height));});
	}
}



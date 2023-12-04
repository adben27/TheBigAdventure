package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Obstacle;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class Main {
  static int x = 1;
  static int y = 1;
  
  public static void main(String[] args) throws IOException {
  	
  	String skin = "pnj/baba";
  	var pts = new Point(1, 1);
  	var baba = new Player("baba", skin, 20, pts);
  	BufferedImage image;
  	try(var input = Main.class.getResourceAsStream("img/" + skin + ".png")) {
  		image = ImageIO.read(input);
  	}
  	
  	Obstacle[][] grid = new Obstacle[20][20];
  	for (int i = 0; i < grid.length; i++) {
  		for (int j = 0; j < grid[i].length; j++) {
  			if (i == 0 || j == 0 || i == grid.length-1 || j == grid[i].length-1) {
  	      grid[i][j] = new Obstacle("obstacle/wall", new Point(i,j));
  	    } else if (i == 7 && j < 16) {
  	      grid[i][j] = new Obstacle("obstacle/pillar", new Point(i,j));
  	    } else if (i == 14 && j > 4) {
  	    	grid[i][j] = new Obstacle("obstacle/pillar", new Point(i,j));
	      } else {
  	      grid[i][j] = new Obstacle("", new Point(i,j));
  	    }
  	  }
  	}

    Application.run(Color.BLACK, context -> {
      
      var TBA = new Graphic(context.getScreenInfo(), grid.length, grid[0].length);
      System.out.println(Graphic.nbCasesX);
      System.out.println(Graphic.nbCasesY);
      context.renderFrame(map -> {
      	try {
					TBA.printMap(grid, map);
				} catch (IOException e) {e.printStackTrace();}
      	map.drawImage(image, Graphic.graphShiftX(x), Graphic.graphShiftY(y), null);
      });
      for(;;) {
        Event event = context.pollOrWaitEvent(10);
        if (event == null) {continue;}
        Action action = event.getAction();
        if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
          context.exit(0);
          return;
        }
        
        if (action == Action.KEY_PRESSED) {
          context.renderFrame(move -> {
          	KeyboardKey key = event.getKey();
            switch (key) {
    					case UP: {
    						if (grid[baba.position.x][baba.position.y-1].skin().equals("")) {
    							try {
										TBA.playerMove(move, baba, 0, -1);
									} catch (IOException e) {e.printStackTrace();}
    						}
    						break;
    					}
    					case DOWN: {
    						if (grid[baba.position.x][baba.position.y+1].skin().equals("")) {
    							try {
										TBA.playerMove(move, baba, 0, 1);
									} catch (IOException e) {e.printStackTrace();}
    						}
    						break;
    					}
    					case LEFT: {
    						if (grid[baba.position.x-1][baba.position.y].skin().equals("")) {
    							try {
										TBA.playerMove(move, baba, -1, 0);
									} catch (IOException e) {e.printStackTrace();}
    						}
    						break;
    					}
    					case RIGHT: {
    						if (grid[baba.position.x+1][baba.position.y].skin().equals("")) {
    							try {
										TBA.playerMove(move, baba, 1, 0);
									} catch (IOException e) {e.printStackTrace();}
    						}
    						break;
    					}
    					default:
    						break;
    				}
          });
        }
      }
    });
  }
}

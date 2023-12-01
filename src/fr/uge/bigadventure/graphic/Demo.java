package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.element.Obstacle;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class Demo {
  static int x = 1;
  static int y = 1;
  
  public static void main(String[] args) throws IOException {
  	
  	String skin = "pnj/baba";
  	BufferedImage image;
  	try(var input = Demo.class.getResourceAsStream("img/" + skin + ".png")) {
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
      
      var TBA = new Graphic(context.getScreenInfo());
      context.renderFrame(map -> {
      	try {
					TBA.printMap(grid, map);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      	map.drawImage(image,x*24, y*24, null);
      });
      for(;;) {
        Event event = context.pollOrWaitEvent(10);
        if (event == null) {  // no event
          continue;
        }
        Action action = event.getAction();
        if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
          System.out.println("abort abort !");
          context.exit(0);
          return;
        }
        System.out.println(event);
        int oldx = x;
        int oldy = y;
        if (action == Action.KEY_PRESSED) {
          KeyboardKey key = event.getKey();
          switch (key) {
  					case UP: {
  						if (y > 0 && grid[x][y-1].skin().equals("")) {y-=1;}
  						break;
  					}
  					case DOWN: {
  						if (y < Graphic.getHeight()-1 && grid[x][y+1].skin().equals("")) {y+=1;}
  						break;
  					}
  					case LEFT: {
  						if (x > 0 && grid[x-1][y].skin().equals("")) {x-=1;}
  						break;
  					}
  					case RIGHT: {
  						if (x < Graphic.getWidth()-1 && grid[x+1][y].skin().equals("")) {x+=1;}
  						break;
  					}
  					default:
  						break;
  				}
        }
        context.renderFrame(graphics -> {
          // hide the previous rectangle
          graphics.setColor(Color.BLACK);
          graphics.fill(new Rectangle2D.Float(oldx*24, oldy*24, 24, 24));
          graphics.drawImage(image,x*24, y*24, null);
        });

      }
    });
  }
}

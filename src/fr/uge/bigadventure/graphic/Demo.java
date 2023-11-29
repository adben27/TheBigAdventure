package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.ScreenInfo;

public class Demo {
  static int x = 0;
  static int y = 0;
  
  public static void main(String[] args) throws IOException {
		/*for(var list : grid) {
			for(var elem : list) {
				System.out.println();
			}
		}*/
  	String tileName = "pnj/baba";
  	BufferedImage image;
  	try(var input = Demo.class.getResourceAsStream("img/" + tileName + ".png")) {
  		image = ImageIO.read(input);
  	}
    Application.run(Color.BLACK, context -> {
      
      // get the size of the screen
      ScreenInfo screenInfo = context.getScreenInfo();
      float width = screenInfo.getWidth();
      float height = screenInfo.getHeight();
      
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
        
        if (action == Action.KEY_PRESSED) {
          KeyboardKey key = event.getKey();
          switch (key) {
  					case UP: {
  						if (y > 0) {y-=20;}
  						break;
  					}
  					case DOWN: {
  						if (y < height-20) {y+=20;}
  						break;
  					}
  					case LEFT: {
  						if (x > 0) {x-=20;}
  						break;
  					}
  					case RIGHT: {
  						if (x < width-20) {x+=20;}
  						break;
  					}
  					default:
  						break;
  				}
        }
        context.renderFrame(graphics -> {
          // hide the previous rectangle
          graphics.setColor(Color.BLACK);
          graphics.fill(new Rectangle2D.Float(0, 0, width, height));
          
          graphics.drawImage(image,x, y, null);
        });

      }
    });
  }
}

package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import fr.uge.bigadventure.Input;
import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Parser;
import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class Main {
  
  public static void main(String[] args) throws IOException {
  	var baba = new Player("baba", "pnj/baba", 20, new Point(1, 1));
  	var keke = new Enemy("keke", "pnj/keke", 20, new Point(4, 4), 5);
  	var entityList = List.of(baba, keke);
  	BufferedImage image;
    var path = Path.of("maps/maze.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var grid = Parser.parse(lexer);
    
  	for (var row : grid) {
  		for (var element : row) {
//  			if (i == 0 || j == 0 || i == grid.length-1 || j == grid[i].length-1) {
//  	      grid[i][j] = new Obstacle("obstacle/wall", new Point(i,j));
//  	    } else if (i == 7 && j < 16) {
//  	      grid[i][j] = new Obstacle("obstacle/pillar", new Point(i,j));
//  	    } else if (i == 14 && j > 4) {
//  	    	grid[i][j] = new Obstacle("obstacle/pillar", new Point(i,j));
//	      } else if (i == 4 && j == 4) {
//	      	grid[i][j] = new Obstacle("scenery/flower", new Point(i,j));
//	      } else if (i == 12 && j == 7) {
//	      	grid[i][j] = new Obstacle("scenery/foliage", new Point(i,j));
//	      } else if (i == 6 && j == 14) {
//	      	grid[i][j] = new Obstacle("scenery/vine", new Point(i,j));
//	      }
  			if (element != null) {
  				try(var input = Main.class.getResourceAsStream(element.skin())) {
  					image = ImageIO.read(input);
  				}
  				Graphic.skinMap.putIfAbsent(element.skin(), image);
  			}
  	  }
  	}
  	try(var input = Main.class.getResourceAsStream("img/" + baba.skin() + ".png")) {
			image = ImageIO.read(input);
		}
		Graphic.skinMap.put(baba.skin(), image);
		try(var input = Main.class.getResourceAsStream("img/" + keke.skin() + ".png")) {
			image = ImageIO.read(input);
		}
		Graphic.skinMap.put(keke.skin(), image);
  	
  	

    Application.run(Color.BLACK, context -> {
      
      new Graphic(context.getScreenInfo(), grid);
      
      context.renderFrame(map -> { // mise en place de l'écran de depart
      	Graphic.printMap(map, grid);
      	Graphic.drawEntity(map, entityList);
      });
      
      for(;;) {
      	if (baba.health() <= 0) {
      		context.exit(0);
          return;
      	}
        Event event = context.pollOrWaitEvent(700);
        context.renderFrame(erase ->{Graphic.eraseEntity(erase, grid, entityList);});
        
				Input.keySwitch(Input.randomKey(), grid, keke);
       	if (keke.position.x == baba.position().x && keke.position.y == baba.position().y) {
       		if (baba.reduceHealth(5)) {
       			context.exit(0);
             return;
       		}
        }
        if (event != null) {
        	Action action = event.getAction();
        	if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
        		context.exit(0);
          	return;
        	}
        
        	if (action == Action.KEY_PRESSED) {
        		Input.keySwitch(event.getKey(), grid, baba);
        	}
        }
        context.renderFrame(draw -> {Graphic.drawEntity(draw, entityList);});
      }
    });
  }
}

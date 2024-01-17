package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.uge.bigadventure.Input;
import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Parser;
import fr.uge.bigadventure.element.Behavior;
import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Player;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class Main {
  
  public static void main(String[] args) throws IOException {
  	
  	Graphic.loadImage();
  	
  	var entityList = List.of(baba, keke);

    var path = Path.of("maps/scroll.map");
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var gameMap = Parser.parse(lexer);
    var grid = gameMap.grid();
    
    System.out.println(grid[0][0].toString());

    Application.run(Color.BLACK, context -> {
      
      new Graphic(context.getScreenInfo(), grid);
      
      context.renderFrame(map -> { // mise en place de l'écran de depart
      	Graphic.printMap(map, grid, baba);
      	Graphic.drawEntity(map, entityList);
      });
      
      for(;;) {
        Event event = context.pollOrWaitEvent(100);
        long startEvent = System.currentTimeMillis();
        long timeBetweenEvents = 0;
        
        context.renderFrame(erase ->{Graphic.eraseEntity(erase, grid, entityList);});
        
				//Input.keySwitch(Input.randomKey(), grid, keke);
        if (event != null) {
        	Action action = event.getAction();
        	if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
        		context.exit(0);
          	return;
        	}
        
        	if (action == Action.KEY_PRESSED) {
        		Input.keySwitch(event.getKey(), grid, baba);
            context.renderFrame(map -> { // mise en place de l'écran de depart
            	Graphic.printMap(map, grid, baba);
            });
        	}
        }
        
        context.renderFrame(draw -> {Graphic.drawEntity(draw, entityList);});
        
       	if (keke.position.x == baba.position().x && keke.position.y == baba.position().y) {
       		if (baba.reduceHealth(5)) {
            context.renderFrame(over -> {Graphic.drawGameOver(over);});
          	while (event == null || event.getAction() != Action.POINTER_UP) {
              	event = context.pollOrWaitEvent(10000);
          	}
       			context.exit(0);
       			return;
       		}
        }
       	
      	while (timeBetweenEvents <= 200) {
      		if (event != null && event.getAction() != Action.KEY_RELEASED) {
          	event = context.pollEvent();
      		}
        	timeBetweenEvents = System.currentTimeMillis() - startEvent;
        }
      }
    });
  }
}

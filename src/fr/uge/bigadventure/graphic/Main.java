package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import fr.uge.bigadventure.Input;
import fr.uge.bigadventure.analyser.Lexer;
import fr.uge.bigadventure.analyser.Parser;
import fr.uge.bigadventure.element.Enemy;
import fr.uge.bigadventure.element.Entity;
import fr.uge.bigadventure.element.Friend;
import fr.uge.bigadventure.element.InventoryItem;
import fr.uge.bigadventure.element.Player;
import fr.uge.bigadventure.element.Weapon;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class Main {
  
  public static void main(String[] args) throws IOException {
  	
  	Graphic.loadImage();

    var path = Path.of("maps/scroll.map");

    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var gameMap = Parser.parse(lexer);
    var grid = gameMap.grid();
    var elementList = gameMap.elementList();
    
    var weaponList = new ArrayList<Weapon>();
    var invItemList = new ArrayList<InventoryItem>();
    
    var enemyList = new ArrayList<Enemy>();
    var entityList = new ArrayList<Entity>();
    
    for(var element : elementList) {
    	switch(element) {
    		case Player listPlayer -> entityList.addFirst(listPlayer); // le player est mis en tête de liste, car il ne doit y en avoir qu'un seul
    		case Enemy listEnemy -> {
    			enemyList.add(listEnemy);
    			entityList.add(listEnemy);
    		}
    		case Friend listFriend -> entityList.add(listFriend);
    		case Weapon listWeapon -> weaponList.add(listWeapon);
    		case InventoryItem listInvItem -> invItemList.add(listInvItem);	
    	}
    }

    Application.run(Color.BLACK, context -> {
      
      new Graphic(context.getScreenInfo(), grid);
      
      context.renderFrame(map -> { // mise en place de l'écran de depart
      	Graphic.printMap(map, grid, entityList.get(0));
      	Graphic.drawEntity(map, entityList);
      });
      
      for(;;) {
        Event event = context.pollOrWaitEvent(100);
        long startEvent = System.currentTimeMillis();
        long timeBetweenEvents = 0;
        
        context.renderFrame(erase ->{Graphic.eraseEntity(erase, grid, entityList);});
        
				Input.keySwitch(Input.randomKey(), grid, enemyList.get(0));
        if (event != null) {
        	Action action = event.getAction();
        	if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
        		context.exit(0);
          	return;
        	}
        
        	if (action == Action.KEY_PRESSED) {
						Input.keySwitch(event.getKey(), grid, entityList.get(0));
            context.renderFrame(map -> { // mise en place de l'écran de depart
            	Graphic.printMap(map, grid, entityList.get(0));
            });
        	}
        }
        
        context.renderFrame(draw -> {Graphic.drawEntity(draw, entityList);});
        
        
        // collision entre player et enemy
        // faut fix ça, player c bon. Mais faut dire que si l'entité qui touche est un ennemi on réduit la vide du player
        if (enemyList.get(0).position.x == entityList.get(0).position().x && enemyList.get(0).position().y == entityList.get(0).position().y) {
       		if (entityList.get(0).reduceHealth(5)) {
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

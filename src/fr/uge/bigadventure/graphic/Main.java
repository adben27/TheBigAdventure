package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

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
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.Event.Action;

public class Main {
	private static Player player;
  public static void main(String[] args) throws IOException {
  
    /*if (args.length == 0) {
      System.out.println("Usage: java -jar thebigadventure.jar 	--level <name.map> [--validate]");
      return;
    }*/

    String levelFileName = null;
    boolean validateOption = false;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];

      if (arg.equals("--level")) {
        if (i + 1 < args.length) {
          levelFileName = args[i + 1];
          i++;
        } else {
          System.err.println("Error: --level option requires a value");
          return;
        }
      } else if (arg.equals("--validate")) {
          validateOption = true;
      }
    }

    if(levelFileName != null) {
      if (validateOption) {
        var path = Path.of(levelFileName);
        var text = Files.readString(path);
        var lexer = new Lexer(text);
        Parser.parse(lexer);
        return;
      }
    } else {
      levelFileName = "maps/void.map";
    }
      
    Graphic.loadImage();

    var path = Path.of(levelFileName);
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var gameMap = Parser.parse(lexer);
    var grid = gameMap.grid();
    var elementList = gameMap.elementList();
    
    var invItemList = new ArrayList<InventoryItem>();
    
    var enemyList = new ArrayList<Enemy>();
    var entityList = new ArrayList<Entity>();
    var weaponList = new ArrayList<Weapon>();

    for(var element : elementList) {
    	switch(element) {
    		case Player listPlayer -> player = listPlayer; // il ne doit y avoir qu'un Player
    		case Enemy listEnemy -> {
    			enemyList.add(listEnemy);
    			entityList.add(listEnemy);
    		}
    		case Friend listFriend -> entityList.add(listFriend);
    		case Weapon listWeapon -> weaponList.add(listWeapon);
    		case InventoryItem listInvItem -> invItemList.add(listInvItem);	
    	}
    }
    
    if(Objects.isNull(player)) {
    	throw new IllegalStateException("There should always be a player");
    }
    
    entityList.add(player);

    Application.run(Color.BLACK, context -> {
      
      new Graphic(context.getScreenInfo(), grid);
      
      context.renderFrame(map -> { // mise en place de l'écran de depart
      	Graphic.printMap(map, grid, player);
      	Graphic.drawEntity(map, entityList);
      });
      
      KeyboardKey lastMove = null;
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
        		KeyboardKey key = event.getKey();
						var saveMove = Input.keySwitch(key, grid, player);
						if (saveMove != null) {
							lastMove = saveMove;
						}
						if (Graphic.scrolling()) {
							context.renderFrame(map -> {Graphic.printMap(map, grid, player);});
						}
						if (key == KeyboardKey.SPACE) {
							Input.hit(lastMove, (Player)entityList.get(0), enemyList);
						}
						Player.loot((Player)entityList.get(0), weaponList);
        	}
        }
        
        context.renderFrame(draw -> {
        	Graphic.drawEntity(draw, entityList);
        	Graphic.drawWeapon(draw, weaponList);
        });
        
        
        // collision entre player et enemy
        // faut fix ça, player c bon. Mais faut dire que si l'entité qui touche est un ennemi on réduit la vide du player
        if(!enemyList.isEmpty()) {
        	if (enemyList.get(0).position.x == player.position().x && enemyList.get(0).position().y == player.position().y) {
        		if (player.reduceHealth(enemyList.get(0).damage())) {
              context.renderFrame(over -> {Graphic.drawGameOver(over);});
              while (event == null || event.getAction() != Action.POINTER_UP) {
                  event = context.pollOrWaitEvent(10000);
              }
               context.exit(0);
               return;
             }
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

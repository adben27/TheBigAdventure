package fr.uge.bigadventure.graphic;

import java.awt.Color;
import java.awt.Point;
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
import fr.uge.bigadventure.element.Food;
import fr.uge.bigadventure.element.Friend;
import fr.uge.bigadventure.element.InventoryItem;
import fr.uge.bigadventure.element.Item;
import fr.uge.bigadventure.element.Player;
import fr.uge.bigadventure.element.Weapon;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class Main {
	private static Player player;
	private static Point lastMove;
	private static boolean hit = false;
  private static boolean dryRun = false;
  public static void main(String[] args) throws IOException {
  
    if (args.length == 0) {
      System.out.println("Usage: java -jar thebigadventure.jar 	--level <name.map> [--validate] [--dry-run]");
      return;
    }

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
      } else if (arg.equals("--dry-run")) {
      	dryRun = true;
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
      System.out.println("Usage: java -jar thebigadventure.jar --level <name.map> [--validate] [--dry-run]");
      return;
    }
      
    Graphic.loadImage();

    var path = Path.of(levelFileName);
    var text = Files.readString(path);
    var lexer = new Lexer(text);
    var gameMap = Parser.parse(lexer);
    var grid = gameMap.grid();
    var elementList = gameMap.elementList();
    
    var invItemList = new ArrayList<Item>();
    
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
    		case Food listFood -> invItemList.add(listFood);
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
      
      for(;;) {
        Event event = context.pollOrWaitEvent(100);
        long startEvent = System.currentTimeMillis();
        long timeBetweenEvents = 0;
        
        context.renderFrame(erase ->{
        	Graphic.eraseEntity(erase, grid, entityList);
        	if (hit) {
        		Graphic.eraseHit(erase, grid, player, lastMove);
        		hit = false;
        	}
        });
        

				if(!dryRun) {
					for(var enemy : enemyList) {
						Input.keySwitch(Input.randomKey(), grid, enemy);
					}
				}
        if (event != null) {
        	Action action = event.getAction();
        	if (action == Action.POINTER_DOWN || action == Action.POINTER_UP) {
        		context.exit(0);
          	return;
        	}
        
        	if (action == Action.KEY_PRESSED) {
        		KeyboardKey key = event.getKey();
						var saveMove = Input.keySwitch(key, grid, player);
						Player.loot(player, weaponList, invItemList);
						if (saveMove != null) {
							lastMove = saveMove;
						}
						if (key == KeyboardKey.SPACE && player.currentWeapon() != null) {
							hit = true;
							Input.hit(lastMove, player, enemyList, entityList);
						}
						if (key == KeyboardKey.I) {
							Graphic.openInventory(context, player);
			        context.renderFrame(map -> {
								Graphic.printMap(map, grid, player);
			        });
						}
        	}
        }
        
        context.renderFrame(draw -> {
					if (Graphic.scrolling()) {
						Graphic.printMap(draw, grid, player);
					}
					if (hit) {
						Graphic.drawHit(draw, lastMove, player);
					}
        	Graphic.drawEntity(draw, entityList);
        	Graphic.drawItem(draw, weaponList, invItemList);
        	
        });
        
        
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
        
      	while (timeBetweenEvents <= 300) {
      		if (event != null && event.getAction() != Action.KEY_RELEASED) {
          	event = context.pollEvent();
      		}
        	timeBetweenEvents = System.currentTimeMillis() - startEvent;
        }
      }
    });
  }
}

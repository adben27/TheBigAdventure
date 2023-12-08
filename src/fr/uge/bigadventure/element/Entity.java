package fr.uge.bigadventure.element;

import java.awt.Point;

public sealed interface Entity extends Element permits Player, Enemy {
	
	Point position();
	String skin();
	int health();
	boolean reduceHealth(int damage);
	
}

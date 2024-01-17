package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public sealed interface Entity extends Element permits Player, Enemy, Friend {

	String name();
	Point position();
	String skin();
	int health();
	int initialHealth();
	boolean reduceHealth(int damage);
	
	public static void entityMove(Entity entity, int moveX, int moveY) { // Prend un Player et le déplace
		Objects.requireNonNull(entity);
    entity.position().x += moveX;
    entity.position().y += moveY;
	}
	
}

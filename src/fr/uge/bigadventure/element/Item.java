package fr.uge.bigadventure.element;

import java.awt.Point;

public sealed interface Item extends Element permits InventoryItem, Weapon {
	String skin();
	Point position();
}

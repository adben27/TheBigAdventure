package fr.uge.bigadventure;

import java.util.ArrayList;
import java.util.Objects;

import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.Obstacle;

public class GameMap {
	private final Obstacle[][] grid;
	private final ArrayList<Element> elementList;

	public GameMap(Obstacle[][] grid, ArrayList<Element> elementList) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(elementList);	
		this.grid = grid;
		this.elementList = elementList;
	}
	
	public void add(Element element) {
		Objects.requireNonNull(element);
		elementList.add(element);
	}
}

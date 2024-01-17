package fr.uge.bigadventure;

import java.util.ArrayList;
import java.util.Objects;

import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.GridElement;

public class GameMap {
	private final GridElement[][] grid;
	private final ArrayList<Element> elementList;

	public GameMap(GridElement[][] grid, ArrayList<Element> elementList) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(elementList);	
		this.grid = grid;
		this.elementList = elementList;
	}
	
	public void add(Element element) {
		Objects.requireNonNull(element);
		elementList.add(element);
	}
	
	public GridElement[][] grid() {
		return grid;
	}
	
	public ArrayList<Element> elementList() {
		return elementList;
	}
}

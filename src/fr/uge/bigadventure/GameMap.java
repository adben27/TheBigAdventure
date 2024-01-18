package fr.uge.bigadventure;

import java.util.ArrayList;
import java.util.Objects;

import fr.uge.bigadventure.element.Element;
import fr.uge.bigadventure.element.GridElement;

public class GameMap {
	private final GridElement[][] grid;
	private final ArrayList<Element> elementList;

	/** Creates a GameMap that contains the grid of the map and its list of elements
	 * 
	 * @param grid The grid of the .map file
	 * @param elementList The list of elements of the .map file
	 */
	public GameMap(GridElement[][] grid, ArrayList<Element> elementList) {
		Objects.requireNonNull(grid);
		Objects.requireNonNull(elementList);	
		this.grid = grid;
		this.elementList = elementList;
	}

	/** Adds a non-null element to the list of elements
	 * 
	 * @param element the element to add to the list of elements of this map
	 */
	public void add(Element element) {
		Objects.requireNonNull(element);
		elementList.add(element);
	}
	
	/** Gets the grid of the map
	 * 
	 * @return the grid of the map
	 */
	public GridElement[][] grid() {
		return grid;
	}
	
	/** Gets the list of elements of the map
	 * 
	 * @return the list of elements of the map
	 */
	public ArrayList<Element> elementList() {
		return elementList;
	}
}

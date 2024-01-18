package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Obstacle(String skin, Point position) implements GridElement {

	/** Creates an Obstacle
	 * 
 	 * @param skin The partial skin path of this Obstacle
	 * @param position The position of this Obstacle
	 */
	public Obstacle {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

	@Override
	public boolean isWalkable() {
		return false;
	}
	
}

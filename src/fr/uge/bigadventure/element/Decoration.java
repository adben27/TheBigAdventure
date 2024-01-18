package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Decoration(String skin, Point position) implements GridElement {
		
	/** Creates a Decoration
	 * 
 	 * @param skin The partial skin path of this Decoration
	 * @param position The position of this Decoration
	 */
	public Decoration {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

	@Override
	public boolean isWalkable() {
		return true;
	}
}

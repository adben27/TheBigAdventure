package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Decoration(String skin, Point position) implements GridElement {
	
	public Decoration {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}
	
	static final String KIND = "scenery/";
	
//	@Override
//	public boolean isWalkable() {
//		return true;
//	}
	
}

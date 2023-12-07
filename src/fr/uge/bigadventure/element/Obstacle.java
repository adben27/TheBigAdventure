package fr.uge.bigadventure.element;

import java.awt.Point;
import java.nio.file.Files;
import java.util.Objects;

public record Obstacle(String skin, Point position) implements GridElement {
	// Pour l'instant, tout Obstacle est inanimé. Il faudra distinguer les objets inanimés des Door par exemple
	static final String KIND = "obstacle/";
	
	public Obstacle {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

	@Override
	public boolean isWalkable() {
		return false;
	}

}

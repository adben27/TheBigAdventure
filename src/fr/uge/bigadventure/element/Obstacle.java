package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Obstacle(String skin, Point position) implements GridElement {
	public static Set<String> obstacleSet = new HashSet<>();
	// Pour l'instant, tout Obstacle est inanimé. Il faudra distinguer les objets inanimés des Door par exemple
	static final String KIND = "obstacle/";
	
	public Obstacle {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}

//	@Override
//	public boolean isWalkable() {
//		return false;
//	}

}

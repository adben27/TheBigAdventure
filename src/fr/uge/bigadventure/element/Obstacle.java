package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Obstacle(String skin, Point position) implements Element{
	// Pour l'instant, tout Obstacle est inanimé. Il faudra distinguer les objets inanimés des Door par exemple.
	public Obstacle {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
	}
}

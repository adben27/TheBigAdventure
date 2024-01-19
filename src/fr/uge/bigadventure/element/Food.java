package fr.uge.bigadventure.element;

import java.awt.Point;
import java.util.Objects;

public record Food(String skin, Point position, int bonus) implements Item {
	
	public Food {
		Objects.requireNonNull(skin);
		Objects.requireNonNull(position);
		if(bonus <= 0) {
			throw new IllegalArgumentException("bonus should not be zero or less");
		}
	}
}

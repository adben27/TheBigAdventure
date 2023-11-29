package fr.uge.bigadventure.element;

import java.nio.file.Path;
import java.util.Objects;

public class Player implements Element {
	private final String name;
	private final Path image;
	private final int health;
	
	public Player(String name, String image, int health) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(image);
		this.name = name;
		this.image = Path.of(image);
		this.health = health;
	}
	
}

package fr.uge.bigadventure.element;

import java.nio.file.Path;

public enum Decoration implements Element {
	ALGAE("scenery/algae.png"),
	CLOUD("scenery/cloud.png"),
	FLOWER("scenery/flower.png"),
	FOLIAGE("scenery/foliage.png"),
	GRASS("scenery/grass.png"),
	LADDER("scenery/ladder.png"),
	LILY("scenery/lily.png"),
	PLANK("scenery/plank.png"),
	REED("scenery/reed.png"),
	ROAD("scenery/road.png"),
	SPROUT("scenery/sprout.png"),
	TILE("scenery/tile.png"),
	TRACK("scenery/track.png"),
	VINE("scenery/vine.png"),
	;
	
	public Path image;
	
	private Decoration(String image) {
		this.image = Path.of(image);
	}
	
}

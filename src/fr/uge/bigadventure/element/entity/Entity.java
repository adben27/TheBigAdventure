package fr.uge.bigadventure.element.entity;

import fr.uge.bigadventure.element.Element;

public sealed interface Entity extends Element permits Player, Enemy {

}

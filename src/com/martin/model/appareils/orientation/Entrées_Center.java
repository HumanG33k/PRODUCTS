package com.martin.model.appareils.orientation;

import com.martin.model.appareils.Direction;

public class Entr�es_Center implements Entr�es {

	@Override
	public Direction getPointerEnter(Direction direction) {
		switch(direction) {
		case UP:		
			return Direction.DOWN;
		case RIGHT:
			return Direction.RIGHT;
		case DOWN:
			return Direction.UP;
		case LEFT:
			return Direction.LEFT;
		default:
			return direction;
		}
	}
	
}

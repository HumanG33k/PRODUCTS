package com.martin.model.appareils.orientation;

import java.util.ArrayList;

import com.martin.model.appareils.Direction;

public class Entr�es_Right implements Entr�es {

	@Override
	public ArrayList<Direction> getPointers(Direction direction) {
		
		ArrayList<Direction> liste = new ArrayList<Direction>();
		
		switch(direction) {
		case UP:		
			liste.add(Direction.LEFT);
		case RIGHT:
			liste.add(Direction.DOWN);
		case DOWN:
			liste.add(Direction.RIGHT);
		case LEFT:
			liste.add(Direction.UP);
		default:
			liste.add(Direction.NONE);
		}
		
		return liste;
	}

}

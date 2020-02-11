package com.martin.model.appareils.orientation;

import java.util.ArrayList;

import com.martin.model.appareils.Direction;

public class Entr�es_LeftAndCenter implements Entr�es {

	@Override
	public ArrayList<Direction> getPointers(Direction direction) {
		
		ArrayList<Direction> liste = new ArrayList<Direction>();
		
		switch(direction) {
		case UP:			
			liste.add(Direction.RIGHT);
			liste.add(Direction.DOWN);
		case RIGHT:
			liste.add(Direction.UP);
			liste.add(Direction.RIGHT);
		case DOWN:
			liste.add(Direction.LEFT);
			liste.add(Direction.UP);
		case LEFT:
			liste.add(Direction.DOWN);
			liste.add(Direction.LEFT);
		default:
			liste.add(Direction.NONE);
		}
		
		return liste;
	}

}

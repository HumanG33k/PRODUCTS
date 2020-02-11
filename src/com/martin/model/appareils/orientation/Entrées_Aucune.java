package com.martin.model.appareils.orientation;

import java.util.ArrayList;

import com.martin.model.appareils.Direction;

public class Entr�es_Aucune implements Entr�es {

	@Override
	public ArrayList<Direction> getPointers(Direction direction) {
		/*Ici, inutile de chercher une entr�e, puisque de toute fa�on l'appareil n'aura aucune 
		 * entr�es*/
		ArrayList<Direction> liste = new ArrayList<Direction>();
		liste.add(Direction.NONE);
		return  liste;
	}

}

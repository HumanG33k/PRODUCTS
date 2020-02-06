package com.martin.model.appareils.orientation;

import com.martin.model.appareils.Direction;

public class Entr�es_Aucune implements Entr�es {

	@Override
	public Direction getPointerEnter(Direction direction) {
		/*Ici, inutile de chercher une entr�e, puisque de toute fa�on l'appareil n'aura aucune 
		 * entr�es*/
		return Direction.NONE;
	}

}

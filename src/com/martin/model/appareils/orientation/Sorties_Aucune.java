package com.martin.model.appareils.orientation;

import com.martin.model.appareils.Direction;

public class Sorties_Aucune implements Sorties {

	@Override
	public Direction getPointer(Direction direction) {
		/*Cette m�thode ne fait rien ici*/
		return Direction.NONE;
	}

}

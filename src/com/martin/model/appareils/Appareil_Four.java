package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.Coordonn�es;
import com.martin.model.appareils.comportement.Comportement_Four;
import com.martin.model.appareils.orientation.Entr�es_Center;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.view.JeuContr�le;

public class Appareil_Four extends Appareil {

	public Appareil_Four(Coordonn�es xy, Direction direction,
			NiveauAppareil niveau, JeuContr�le controller)
			throws FileNotFoundException {
		super(xy, TypeAppareil.FOUR, direction, niveau, controller);

		entr�es = new Entr�es_Center();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Center();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Four(xy, niveau, pointerExit.getxPlus(),
				pointerExit.getyPlus(), controller);
	}
}

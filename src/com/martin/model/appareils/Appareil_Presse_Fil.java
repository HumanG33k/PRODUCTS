package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.Coordonnees;
import com.martin.model.appareils.comportement.Comportement_Presse_Fil;
import com.martin.model.appareils.orientation.Entr�es_Center;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.view.JeuContr�le;

public class Appareil_Presse_Fil extends Appareil {

	public Appareil_Presse_Fil() {
	}

	public Appareil_Presse_Fil(Coordonnees xy, Direction direction,
			NiveauAppareil niveau,
			JeuContr�le controller)
			throws FileNotFoundException {
		super(xy, TypeAppareil.PRESSE_FIL, direction, niveau, controller);

		entr�es = new Entr�es_Center();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Center();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Presse_Fil(xy, niveau,
				pointerExit.getxPlus(),
				pointerExit.getyPlus(), controller);
	}

}
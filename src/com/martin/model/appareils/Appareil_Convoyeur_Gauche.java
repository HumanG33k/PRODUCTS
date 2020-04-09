package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.view.JeuContr�le;

public class Appareil_Convoyeur_Gauche extends Appareil {

	public Appareil_Convoyeur_Gauche(AppareilModel model,
			JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		entrances = Entr�es.listForUp(model.getDirection());
		exits = Sorties.listForLeft(model.getDirection());

		// Todo : add behaviour
	}
}

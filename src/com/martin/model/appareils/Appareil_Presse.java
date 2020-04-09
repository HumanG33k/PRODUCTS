package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.view.JeuContr�le;

public class Appareil_Presse extends Appareil {

	public Appareil_Presse(AppareilModel model, JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		entrances = Entr�es.listForUp(model.getDirection());
		exits = Sorties.listForCenter(model.getDirection());

		// Todo : add behaviour
	}
}
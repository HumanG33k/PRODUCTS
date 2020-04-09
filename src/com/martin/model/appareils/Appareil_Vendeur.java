package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.Stock;
import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Appareil_Vendeur extends Appareil {

	public Appareil_Vendeur(AppareilModel model, JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		exits = Entr�es.listForBottom(model.getDirection());
		entrances = Sorties.listForNone();

		// Todo : add behaviour
	}

	@Override
	public void action(Stock resATraiter) throws NegativeArgentException {
		comportement.action(resATraiter);
	}

}
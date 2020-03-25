package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.Coordonnees;
import com.martin.model.Stock;
import com.martin.model.appareils.comportement.Comportement_Vendeur;
import com.martin.model.appareils.orientation.Entr�es_Center;
import com.martin.model.appareils.orientation.Sorties_Aucune;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Appareil_Vendeur extends Appareil {

	public Appareil_Vendeur() {
	}

	public Appareil_Vendeur(Coordonnees xy, Direction direction,
			NiveauAppareil niveau, JeuContr�le controller)
			throws FileNotFoundException {
		super(xy, TypeAppareil.VENDEUR, direction, niveau, controller);

		entr�es = new Entr�es_Center();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Aucune();
		pointerExit = sorties.getPointer(direction);

		comportement = new Comportement_Vendeur(xy, niveau,
				direction.getxPlus(), direction.getyPlus(), controller);
	}

	@Override
	public void action(Stock resATraiter) throws NegativeArgentException {
		comportement.action(resATraiter);
	}

}
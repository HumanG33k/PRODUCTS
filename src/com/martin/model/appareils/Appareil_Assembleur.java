package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Assembleur;
import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.view.JeuContr�le;

public class Appareil_Assembleur extends Appareil {

	ArrayList<Ressource> recette = new ArrayList<Ressource>();

	public Appareil_Assembleur(AppareilModel model, JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		entrances = Entr�es.listForLeft(model.getDirection());
		entrances.addAll(Entr�es.listForUp(model.getDirection()));
		exits = Sorties.listForRight(model.getDirection());

		// Todo : add behaviour
	}

	public void setProduit(Ressource res) throws NullPointerException {
		if (behaviour instanceof Comportement_Assembleur) {
			((Comportement_Assembleur) behaviour).setProduit(res);
		}
	}

	public Ressource getProduit() throws NullPointerException {
		if (behaviour instanceof Comportement_Assembleur)
			return ((Comportement_Assembleur) behaviour).getProduit()
					.getRessource();
		return null;
	}
}

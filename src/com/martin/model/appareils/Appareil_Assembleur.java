package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Assembleur;
import com.martin.model.appareils.orientation.Entr�es_LeftAndCenter;
import com.martin.model.appareils.orientation.Sorties_Right;
import com.martin.view.JeuContr�le;

public class Appareil_Assembleur extends Appareil {

	ArrayList<Ressource> recette = new ArrayList<Ressource>();

	public Appareil_Assembleur(Coordonn�es xy, NiveauAppareil niveau,
			Direction direction, JeuContr�le controller)
			throws FileNotFoundException {
		super(xy, TypeAppareil.ASSEMBLEUR, direction, niveau, controller);

		entr�es = new Entr�es_LeftAndCenter();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Right();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Assembleur(xy, niveau,
				pointerExit.getxPlus(),
				pointerExit.getyPlus(), controller);
	}

	public void setProduit(Ressource res) throws NullPointerException {
		if (comportement instanceof Comportement_Assembleur) {
			((Comportement_Assembleur) comportement).setProduit(res);
		}
	}

	public Ressource getProduit() throws NullPointerException {
		if (comportement instanceof Comportement_Assembleur)
			return ((Comportement_Assembleur) comportement).getProduit()
					.getRessource();
		return null;
	}
}

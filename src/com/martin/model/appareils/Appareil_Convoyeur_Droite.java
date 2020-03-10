package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.Coordonn�es;
import com.martin.model.appareils.comportement.Comportement_Convoyeur;
import com.martin.model.appareils.orientation.Entr�es_Center;
import com.martin.model.appareils.orientation.Sorties_Right;
import com.martin.view.JeuContr�le;

import javafx.beans.property.SimpleIntegerProperty;

public class Appareil_Convoyeur_Droite extends Appareil {

	private static SimpleIntegerProperty prix;

	public Appareil_Convoyeur_Droite(Coordonn�es xy, Direction direction,
			NiveauAppareil niveau, JeuContr�le controller)
			throws FileNotFoundException {
		super(xy, TypeAppareil.CONVOYEUR_DROITE, direction, niveau, controller);

		entr�es = new Entr�es_Center();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Right();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Convoyeur(xy, niveau,
				pointerExit.getxPlus(),
				pointerExit.getyPlus(), controller);
	}

	public static void initializeData() {
		prix = new SimpleIntegerProperty();
		prix.bind(Appareil_Convoyeur.prix);
	}

	public static int getPrix() {
		return prix.get();
	}
}

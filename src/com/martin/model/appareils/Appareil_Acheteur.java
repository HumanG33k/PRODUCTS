package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.Connect_SQLite;
import com.martin.Partie;
import com.martin.model.Coordonn�es;
import com.martin.model.Paquet;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Acheteur;
import com.martin.model.appareils.orientation.Entr�es_Aucune;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.view.JeuContr�le;

import javafx.scene.paint.Color;

public class Appareil_Acheteur extends Appareil {

	public static ArrayList<Coordonn�es> liste = new ArrayList<Coordonn�es>();

	public Appareil_Acheteur(Coordonn�es xy, Direction direction,
			NiveauAppareil niveau,
			JeuContr�le controller, Partie partie)
			throws FileNotFoundException {

		super(xy, TypeAppareil.ACHETEUR, direction, niveau, controller, partie);
		liste.add(xy);

		entr�es = new Entr�es_Aucune();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Center();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Acheteur(xy, niveau,
				pointerExit.getxPlus(),
				pointerExit.getyPlus(), controller);
	}

	@Override
	public void destruction() {
		try {
			Connect_SQLite.getAppareilDao().delete(this);
		} catch (Exception e) {
			controller.setReport("L'appareil n'a pas �t� d�truit correctement.",
					Color.DARKRED);
		}
	}

	/**
	 * 
	 * @return the ditributed resource
	 * @throws NullPointerException if the behaviour of this device isn't
	 *                              a buyer
	 */
	public Ressource getRessourceDistribu�e() throws NullPointerException {
		if (comportement instanceof Comportement_Acheteur)
			return ((Comportement_Acheteur) comportement)
					.getRessourceDistribu�e().getRessource();
		return null;
	}

	/**
	 * 
	 * @param res the new value of the property
	 */
	public void setRessourceDistribu�e(Ressource res) {
		if (comportement instanceof Comportement_Acheteur) {
			((Comportement_Acheteur) comportement)
					.setRessourceDistribu�e(new Paquet(res, 1));
		}
	}
}
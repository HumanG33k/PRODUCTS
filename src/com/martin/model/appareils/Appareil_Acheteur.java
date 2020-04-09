package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.model.Coordonnees;
import com.martin.model.Paquet;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Acheteur;
import com.martin.model.appareils.orientation.Entrées;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.view.JeuContrôle;

public class Appareil_Acheteur extends Appareil {

	public static ArrayList<Coordonnees> liste = new ArrayList<Coordonnees>();

	public Appareil_Acheteur(AppareilModel model, JeuContrôle controller)
			throws FileNotFoundException {

		super(model, controller);
		liste.add(model.getCoordonnees());

		entrances = Entrées.listForNone();
		entrances = Sorties.listForCenter(model.getDirection());

		// Todo: add behaviour
	}

	/**
	 * 
	 * @return the ditributed resource
	 * @throws NullPointerException if the behaviour of this device isn't
	 *                              a buyer
	 */
	public Ressource getRessourceDistribuée() throws NullPointerException {
		if (comportement instanceof Comportement_Acheteur)
			return ((Comportement_Acheteur) comportement)
					.getRessourceDistribuée().getRessource();
		return null;
	}

	/**
	 * 
	 * @param res the new value of the property
	 */
	public void setRessourceDistribuée(Ressource res) {
		if (comportement instanceof Comportement_Acheteur) {
			((Comportement_Acheteur) comportement)
					.setRessourceDistribuée(new Paquet(res, 1));
		}
	}
}
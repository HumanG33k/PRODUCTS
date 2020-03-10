package com.martin.model.appareils.comportement;

import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonn�es;
import com.martin.model.Paquet;
import com.martin.model.Ressource;
import com.martin.model.Stock;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Comportement_Acheteur implements Comportement {

	private Coordonn�es pointer;
	private NiveauAppareil niveau;
	private JeuContr�le controller;

	private Paquet resDistribu�e;

	public Comportement_Acheteur(Coordonn�es xy, NiveauAppareil niveau,
			int xToAdd, int yToAdd, JeuContr�le controller) {
		this.niveau = niveau;
		this.controller = controller;
		this.pointer = new Coordonn�es(xy.getX() + xToAdd, xy.getY() + yToAdd);

		// FixMe : charger la ressource
	}

	@Override
	public void action(Stock resATraiter) throws NegativeArgentException {
		final Stock tempoStock = new Stock();

		for (int niveau = 0; this.niveau.getNiveau() < niveau; niveau++) {

			if (!resDistribu�e.getRessource().equals(Ressource.NONE)) {
				if (controller.getPartieEnCours().getArgent() < 5
						+ Appareil.get�lectricit�())
					throw new NegativeArgentException("Le comportement d'un "
							+ "acheteur n'a pas pu �tre r�alis� car le solde "
							+ "d'argent n'�tait pas assez important.");
				else {
					tempoStock.add(resDistribu�e);
					controller.setArgent(5 + Appareil.get�lectricit�(), false);
				}
			}
		}

		controller.getGrilleAppareils(pointer).action(tempoStock);
	}

	/**
	 * <h1>setProduit</h1>
	 * <p>
	 * Sets the products to the new value, after checking if it is a valid
	 * resource.
	 * </p>
	 * 
	 * @param produit the resource to set
	 */
	public void setRessourceDistribu�e(Paquet resDistribu�e) {
		switch (resDistribu�e.getRessource()) {
		case FER:
		case OR:
		case CUIVRE:
		case ARGENT:
		case DIAMANT:
		case ALUMINIUM:
			for (int i = 0; i < this.niveau.getNiveau(); i++) {

				try {
					Connect_SQLite.getPaquetDao().createIfNotExists(
							resDistribu�e);
					this.resDistribu�e = resDistribu�e;
				} catch (SQLException e) {
					System.err.println(e.getLocalizedMessage());

				}
			}
		default:
			break;
		}

	}

	/**
	 * 
	 * @return resDistribu�e the distributed resource
	 */
	public Paquet getRessourceDistribu�e() {
		return resDistribu�e;
	}
}
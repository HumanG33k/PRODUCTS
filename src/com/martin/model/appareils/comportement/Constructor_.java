package com.martin.model.appareils.comportement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.martin.Database;
import com.martin.model.Coordinates;
import com.martin.model.Packing;
import com.martin.model.Resource;
import com.martin.model.Stock;
import com.martin.model.appareils.Device;
import com.martin.model.appareils.DeviceModel;
import com.martin.model.appareils.Level;
import com.martin.model.exceptions.MoneyException;
import com.martin.view.JeuContr�le;

public class Constructor_ implements Behaviour {
	private Level level;
	private JeuContr�le controller;

	private Packing produit;
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private ArrayList<Resource> recipes = new ArrayList<Resource>();

	public Constructor_(DeviceModel model, JeuContr�le controller) {
		this.level = model.getNiveau();
		this.controller = controller;

		try {
			// Query for all the packages that are associated to this device
			final List<Packing> list = Database.daoPacking().queryBuilder()
					.where().eq("device", model.getIdAppareilModel()).query();
			// If its size equals 0, then create the resource and save it in the
			// database
			if (list.size() == 0) {
				produit = new Packing(Resource.NONE, 1, model);
				Database.daoPacking().create(produit);
			}
			// Else we get at the first index the packing
			else {
				produit = list.get(0);
			}

			// If the list is bigger than 1, there is an error (the resource was
			// added by the user (not in game)).
			// So the rest just doesn't matter
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage());

		}
	}

	@Override
	public void action(Stock resATraiter, Coordinates pointer)
			throws MoneyException {
		Stock tempoStock = new Stock();

		for (int level = 0; level < this.level.getNiveau()
				|| level < resATraiter.size(); level++) {
			if (controller.getPartieEnCours().getArgent() < 5
					+ Device.getElectricity())
				throw new MoneyException(
						"Le comportement d'un acheteur "
								+ "n'a pas pu �tre r�alis� car le solde "
								+ "d'argent n'�tait pas assez important.");

			if (checkIngr�dients()) {
				tempoStock.add(produit);
				controller.setArgent(Device.getElectricity(), false);
			}
		}

		controller.findDevice(pointer).action(tempoStock);
	}

	/**
	 * 
	 * <h1>checkIngr�dients</h1>
	 * <p>
	 * Checks if thre are enough resources to built the product
	 * </p>
	 * 
	 * @return boolean if the requires resources are available
	 * 
	 */
	private boolean checkIngr�dients() {
		// Le stock temporaire pour mettre les de c�t� les ressources
		// r�serv�es au produit
		ArrayList<Resource> stock = new ArrayList<Resource>();
		// On vide les �l�ments de la recette
		recipes = new ArrayList<Resource>();
		// Puis on la re-remplie en fonction des ressources de la quantit�
		// Cette appareil prend en charge tous les sch�ma � 2 paquets
		for (int i = 0; i < produit.getRessource().getRecette().get(0)
				.getQuantit�(); i++) {
			recipes.add(
					produit.getRessource().getRecette().get(0).getRessource());
		}
		for (int i = 0; i < produit.getRessource().getRecette().get(1)
				.getQuantit�(); i++) {
			recipes.add(
					produit.getRessource().getRecette().get(1).getRessource());
		}

		// Pour la taille de la recette cr�e
		for (int j = 0; j < produit.getRessource().getRecette().size(); j++) {
			// Si la ressourc est pr�sente dans le stock
			if (resources.contains(recipes.get(j))) {
				// On l'ajoute au stock temporaire et on l'enl�ve du stockage
				stock.add(recipes.get(j));
				resources.remove(recipes.get(j));
			}
			// Sinon...
			else {
				// On remet les ressource du stock dans le stockage principal
				resources.addAll(stock);
				// On retourne faux pour dire que l'action ne peut pas continuer
				// (les ressources ne sont pas suffisantes)
				return false;
			}
		}
		// On retourne vrai car toutes les ressources n�cessaires sont
		// disponibles
		return true;
	}

	/**
	 * Sets the products to the new value, after checking if it is a valid
	 * resource.
	 * 
	 * @param produit the resource to set
	 */
	public void setProduit(Packing produit) {
		switch (produit.getRessource()) {
		case FER:
		case OR:
		case CUIVRE:
		case ARGENT:
		case DIAMANT:
		case ALUMINIUM:
			this.produit = produit;
			try {
				Database.daoPacking().update(produit);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		default:
			break;
		}

	}

	/**
	 * 
	 * @return produit the current product od this device
	 */
	public Packing getProduit() {
		return produit;
	}
}
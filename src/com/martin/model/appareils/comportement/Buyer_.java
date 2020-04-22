package com.martin.model.appareils.comportement;

import java.sql.SQLException;
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

public class Buyer_ implements Behaviour {
	private Level level;
	private JeuContr�le controller;

	private Packing resDistribu�e;

	public Buyer_(DeviceModel model, JeuContr�le controller) {
		this.level = model.getNiveau();
		this.controller = controller;

		try {
			// Query for all the packages that are associated to this device
			final List<Packing> list = Database.daoPacking().queryBuilder()
					.where().eq("device", model.getIdAppareilModel()).query();
			// If its size equals 0, then create the resource and save it in the
			// database
			if (list.size() == 0) {
				resDistribu�e = new Packing(Resource.NONE, 1, model);
				Database.daoPacking().create(resDistribu�e);
			}
			// Else we get at the first index the packing
			else {
				resDistribu�e = list.get(0);
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
		System.out.println("The behaviour of a buyer has begun !");

		final Stock tempoStock = new Stock();

		for (int niveau = 0; niveau < this.level.getNiveau(); niveau++) {

			if (!resDistribu�e.getRessource().equals(Resource.NONE)) {
				if (controller.getPartieEnCours().getArgent() < 5
						+ Device.getElectricity())
					throw new MoneyException("Le comportement d'un "
							+ "acheteur n'a pas pu �tre r�alis� car le solde "
							+ "d'argent n'�tait pas assez important.");
				else {
					tempoStock.add(resDistribu�e);
					controller.setArgent(5 + Device.getElectricity(), false);
				}
			} else {
				tempoStock.add(Resource.NONE);
			}
		}

		controller.findDevice(pointer).action(tempoStock);
	}

	/**
	 * Sets the products to the new value, after checking if it is a valid
	 * resource.
	 * 
	 * @param produit the resource to set
	 */
	public void setRessourceDistribu�e(Packing resDistribu�e) {
		switch (resDistribu�e.getRessource()) {
		case FER:
		case OR:
		case CUIVRE:
		case ARGENT:
		case DIAMANT:
		case ALUMINIUM:
			this.resDistribu�e = resDistribu�e;
			try {
				Database.daoPacking().update(resDistribu�e);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		default:
			break;
		}

	}

	/**
	 * 
	 * @return resDistribu�e the distributed resource
	 */
	public Packing getRessourceDistribu�e() {
		return resDistribu�e;
	}
}
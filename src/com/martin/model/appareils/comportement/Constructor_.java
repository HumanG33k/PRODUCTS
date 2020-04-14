package com.martin.model.appareils.comportement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

	private Coordinates pointer;
	private Level level;
	private JeuContr�le controller;

	private Packing produit;
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private ArrayList<Resource> recette = new ArrayList<Resource>();

	public Constructor_(Coordinates xy, Level level,
			int xToAdd, int yToAdd, JeuContr�le controller,
			DeviceModel model) {
		this.level = level;
		this.controller = controller;
		this.pointer = new Coordinates(xy.getX() + xToAdd, xy.getY() + yToAdd);

		Session session = Database.getSession();
		Transaction tx = session.getTransaction();
		try {
			session.beginTransaction();

			Query<Packing> query = session.createQuery(
					"from Paquet where appareil = "
							+ model.getIdAppareilModel(),
					Packing.class);
			List<Packing> list = query.list();

			if (list.size() == 0) {
				produit = new Packing(Resource.NONE, 1, model);
				session.save(produit);
				tx.commit();
			} else if (list.size() == 1) {
				produit = list.get(0);
			}
		} catch (HibernateException e) {
			System.err
					.println("Error when loading resource. Error message :\n");
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public void action(Stock resATraiter) throws MoneyException {
		Stock tempoStock = new Stock();

		for (int level = 0; level < level.getNiveau()
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

		controller.getPartieEnCours().getAppareil(pointer).action(tempoStock);
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
		recette = new ArrayList<Resource>();
		// Puis on la re-remplie en fonction des ressources de la quantit�
		// Cette appareil prend en charge tous les sch�ma � 2 paquets
		for (int i = 0; i < produit.getRessource().getRecette().get(0)
				.getQuantit�(); i++) {
			recette.add(
					produit.getRessource().getRecette().get(0).getRessource());
		}
		for (int i = 0; i < produit.getRessource().getRecette().get(1)
				.getQuantit�(); i++) {
			recette.add(
					produit.getRessource().getRecette().get(1).getRessource());
		}

		// Pour la taille de la recette cr�e
		for (int j = 0; j < produit.getRessource().getRecette().size(); j++) {
			// Si la ressourc est pr�sente dans le stock
			if (resources.contains(recette.get(j))) {
				// On l'ajoute au stock temporaire et on l'enl�ve du stockage
				stock.add(recette.get(j));
				resources.remove(recette.get(j));
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
	 * <h1>setProduit</h1>
	 * <p>
	 * Sets the products to the new value, after checking if it is a valid
	 * resource.
	 * </p>
	 * 
	 * @param produit the resource to set
	 */
	public void setProduit(Resource produit) {
		// On v�rifie que la ressource donn� est prise en charge par
		// l'appareil
		switch (produit) {
		case FER:
		case OR:
		case CUIVRE:
		case ARGENT:
		case DIAMANT:
		case ALUMINIUM:
			break;
		default:
			// Si oui, on modifie le produit
			this.produit = new Packing(produit, 1);
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

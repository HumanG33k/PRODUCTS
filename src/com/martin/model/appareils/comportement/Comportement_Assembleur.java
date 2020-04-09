package com.martin.model.appareils.comportement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonnees;
import com.martin.model.Paquet;
import com.martin.model.Ressource;
import com.martin.model.Stock;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.Niveau;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Comportement_Assembleur implements Comportement {

	private Coordonnees pointer;
	private Niveau niveau;
	private JeuContr�le controller;

	private Paquet produit;
	private ArrayList<Ressource> ressources = new ArrayList<Ressource>();
	private ArrayList<Ressource> recette = new ArrayList<Ressource>();

	public Comportement_Assembleur(Coordonnees xy, Niveau niveau,
			int xToAdd, int yToAdd, JeuContr�le controller, Appareil appareil) {
		this.niveau = niveau;
		this.controller = controller;
		this.pointer = new Coordonnees(xy.getX() + xToAdd, xy.getY() + yToAdd);

		Session session = Connect_SQLite.getSession();
		Transaction tx = session.getTransaction();
		try {
			session.beginTransaction();

			Query<Paquet> query = session.createQuery(
					"from Paquet where appareil = " + appareil.getId(),
					Paquet.class);
			List<Paquet> list = query.list();

			if (list.size() == 0) {
				produit = new Paquet(Ressource.NONE, 1, appareil);
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
	public void action(Stock resATraiter) throws NegativeArgentException {
		Stock tempoStock = new Stock();

		for (int level = 0; level < niveau.getNiveau()
				|| level < resATraiter.size(); level++) {
			if (controller.getPartieEnCours().getArgent() < 5
					+ Appareil.get�lectricit�())
				throw new NegativeArgentException(
						"Le comportement d'un acheteur "
								+ "n'a pas pu �tre r�alis� car le solde "
								+ "d'argent n'�tait pas assez important.");

			if (checkIngr�dients()) {
				tempoStock.add(produit);
				controller.setArgent(Appareil.get�lectricit�(), false);
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
		ArrayList<Ressource> stock = new ArrayList<Ressource>();
		// On vide les �l�ments de la recette
		recette = new ArrayList<Ressource>();
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
			if (ressources.contains(recette.get(j))) {
				// On l'ajoute au stock temporaire et on l'enl�ve du stockage
				stock.add(recette.get(j));
				ressources.remove(recette.get(j));
			}
			// Sinon...
			else {
				// On remet les ressource du stock dans le stockage principal
				ressources.addAll(stock);
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
	public void setProduit(Ressource produit) {
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
			this.produit = new Paquet(produit, 1);
			break;
		}

	}

	/**
	 * 
	 * @return produit the current product od this device
	 */
	public Paquet getProduit() {
		return produit;
	}
}

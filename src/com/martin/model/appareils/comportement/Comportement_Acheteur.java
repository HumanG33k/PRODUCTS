package com.martin.model.appareils.comportement;

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

public class Comportement_Acheteur implements Comportement {

	private Coordonnees pointer;
	private Niveau niveau;
	private JeuContr�le controller;

	private Paquet resDistribu�e;

	public Comportement_Acheteur(Coordonnees xy, Niveau niveau,
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
				resDistribu�e = new Paquet(Ressource.NONE, 1, appareil);
				session.save(resDistribu�e);
				tx.commit();
			} else if (list.size() == 1) {
				resDistribu�e = list.get(0);
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
		final Stock tempoStock = new Stock();

		for (int niveau = 0; niveau < this.niveau.getNiveau(); niveau++) {

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
			} else {
				tempoStock.add(Ressource.NONE);
			}
		}

		controller.getPartieEnCours().getAppareil(pointer).action(tempoStock);
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
			this.resDistribu�e = resDistribu�e;
			Session session = Connect_SQLite.getSession();
			Transaction tx = session.getTransaction();
			try {
				session.beginTransaction();
				session.update(resDistribu�e);
				tx.commit();
			} catch (HibernateException e) {
				System.err
						.println(
								"Error when setting resource. Error message :\n");
				e.printStackTrace();
			} finally {
				session.close();
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
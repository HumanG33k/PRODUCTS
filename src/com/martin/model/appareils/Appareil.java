package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.martin.Connect_SQLite;
import com.martin.Main;
import com.martin.Partie;
import com.martin.model.Coordonnees;
import com.martin.model.LocatedImage;
import com.martin.model.Stock;
import com.martin.model.appareils.comportement.Comportement;
import com.martin.model.appareils.comportement.Comportement_Aucun;
import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Entr�es_Aucune;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.model.appareils.orientation.Sorties_Aucune;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.AppareilsContr�le;
import com.martin.view.Dashboard;
import com.martin.view.JeuContr�le;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public abstract class Appareil extends ImageView {

	protected Long idAppareil;

	protected Partie partie;

	protected TypeAppareil type;
	protected Direction direction;
	protected NiveauAppareil niveau;
	protected Coordonnees xy;

	protected Comportement comportement = new Comportement_Aucun();
	protected Sorties sorties = new Sorties_Aucune();
	protected Entr�es entr�es = new Entr�es_Aucune();

	protected JeuContr�le controller;

	protected static int �lectricit� = 5;
	protected Direction pointerExit;
	protected ArrayList<Direction> pointersEnters;

	protected Dashboard dashboard = new Dashboard();

	public Appareil() {
		super();
		try {
			this.setImage(
					new LocatedImage("images/machines niveau 2/Acheteur.png"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());

		}
	}

	/**
	 * Creates a new device.
	 * 
	 * @param xy         the coordinates of this device
	 * @param type       the kind of the device
	 * @param direction  the rotate of this device
	 * @param niveau     the level of this device
	 * @param controller the game controller
	 * 
	 */
	protected Appareil(Coordonnees xy, TypeAppareil type, Direction direction,
			NiveauAppareil niveau,
			JeuContr�le controller) throws FileNotFoundException {
		super(new LocatedImage(niveau.getURL() + type.getURL()));

		this.xy = xy;
		this.type = type;
		this.direction = direction;
		this.niveau = niveau;
		this.controller = controller;
		if (controller != null)
			this.partie = controller.getPartieEnCours();
		setRotate(direction.getRotate());

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					// Charger et ouvrir la bo�te de dialogue demandant l'action
					// � effectuer
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(
							Main.class.getResource("view/Appareil.fxml"));

					Dialog<TypeAppareil> dialog;
					DialogPane dialogPane;

					dialogPane = (DialogPane) loader.load();
					dialog = new Dialog<TypeAppareil>();
					dialog.setTitle("S�lection d'appareil - PRODUCTS.");
					dialog.setDialogPane(dialogPane);
					dialog.initOwner(Main.stage);
					dialog.initModality(Modality.NONE);

					AppareilsContr�le controller = loader.getController();
					controller.setMainApp(xy.getX(), xy.getY(), dashboard);

					dialog.showAndWait();

				} catch (IOException e) {
					/*
					 * Si une erreur est survenue lors du chargement de la
					 * fen�tre, afficher le message plus la raison donn�e par
					 * Java.
					 */
					System.err.println(
							"ERREUR dans Appareil.java entre les lignes 59 et 79 excluses."
									+ "Raison :\n" + e.getMessage());
				}
			}
		});
	}

	/**
	 * <h1>action</h1>
	 * <p>
	 * This method do the action of the device. It calls the defined
	 * behaviour.
	 * </p>
	 * 
	 * @param resATraiter the resource who will be used by this device
	 */
	public void action(Stock resATraiter) throws NegativeArgentException {
		if (this.controller
				.getPartieEnCours().getAppareil(
						new Coordonnees(xy.getX() + pointerExit.getxPlus(),
								xy.getY() + pointerExit.getyPlus()))
				.getXy().isNearFrom(xy)) {

			for (int i = 0; i < pointersEnters.size(); i++) {
				if (pointerExit.equals(controller
						.getPartieEnCours().getAppareil(new Coordonnees(
								xy.getX() + pointerExit.getxPlus(),
								xy.getY() + pointerExit.getyPlus()))
						.getPointerEnter().get(i)))

					comportement.action(resATraiter);
			}
		}
	}

	public Appareil toInstance(JeuContr�le controller)
			throws NullPointerException {
		try {
			return this.getType().getClasse()
					.getConstructor(Coordonnees.class, Direction.class,
							NiveauAppareil.class, JeuContr�le.class)
					.newInstance(this.xy, this.direction, this.niveau,
							controller);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// GETTERS, THEN SETTERS
	/**
	 * 
	 * @return the id
	 */
	public Long getIdAppareil() {
		return idAppareil;
	}

	/**
	 * @return the type
	 */
	public TypeAppareil getType() {
		return type;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * 
	 * @return the pointerEnter
	 */
	public ArrayList<Direction> getPointerEnter() {
		return pointersEnters;
	}

	/**
	 * 
	 * @return the pointerExit
	 */
	public Direction getPointerExit() {
		return pointerExit;
	}

	/**
	 * @return the niveau
	 */
	public NiveauAppareil getNiveau() {
		return niveau;
	}

	/**
	 * @return the xy
	 */
	public Coordonnees getXy() {
		return xy;
	}

	/**
	 * @return the comportement
	 */
	public Comportement getComportement() {
		return comportement;
	}

	/**
	 * @return the sorties
	 */
	public Sorties getSorties() {
		return sorties;
	}

	/**
	 * @return the entr�es
	 */
	public Entr�es getEntr�es() {
		return entr�es;
	}

	/**
	 * @return the controller
	 */
	public JeuContr�le getController() {
		return controller;
	}

	/**
	 * @return the �lectricit�
	 */
	public static int get�lectricit�() {
		return �lectricit�;
	}

	/**
	 * 
	 * @return partie the partie property
	 */
	public Partie getPartie() {
		return partie;
	}

	/**
	 * 
	 * @param id the new id
	 */
	public void setIdAppareil(Long id) {
		this.idAppareil = id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeAppareil type) {
		this.type = type;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @param niveau the niveau to set
	 */
	public void setNiveau(NiveauAppareil niveau) {
		this.niveau = niveau;
	}

	/**
	 * @param xy the xy to set
	 */
	public void setXy(Coordonnees xy) {
		this.xy = xy;
	}

	/**
	 * @param comportement the comportement to set
	 */
	public void setComportement(Comportement comportement) {
		this.comportement = comportement;
	}

	/**
	 * @param sorties the sorties to set
	 */
	public void setSorties(Sorties sorties) {
		this.sorties = sorties;
	}

	/**
	 * @param entr�es the entr�es to set
	 */
	public void setEntr�es(Entr�es entr�es) {
		this.entr�es = entr�es;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(JeuContr�le controller) {
		this.controller = controller;
	}

	/**
	 * 
	 * @param partie the game to set
	 */
	public void setPartie(Partie partie) {
		this.partie = partie;
	}

	/**
	 * @param �lectricit� the �lectricit� to set
	 */
	public static void set�lectricit�(int �lectricit�) {
		Appareil.�lectricit� = �lectricit�;
	}

	/**
	 * This method returns a List of Appareil, from table appareils. May
	 * be expensive to invoke; if you have to use it, stock the result in
	 * a list.
	 * 
	 * @return a list of devices
	 */
	public static List<Appareil> query() {
		// Creating a Session and a List
		Session session = Connect_SQLite.getSession();
		List<Appareil> list;
		try {
			// Query for all objects and stock it in the List created before
			Query<Appareil> query = session.createQuery(
					"from Coordonnees",
					Appareil.class);
			list = query.list();
		} catch (HibernateException e) {
			System.err.println("Unable to query in table coordonn�es");
			return null;
		} finally {
			// Closing the session
			session.close();
		}
		// Returning the result
		return list;
	}

	/**
	 * Insert in table coordonn�es the object in parameters. May be
	 * expensive to invoke. Checks before inserting if all constraints are
	 * respected. Because Hibernate doesn't fully support SQLite (such as
	 * UniqueCombo constraints), I had to do this before inserting.
	 * 
	 * @param objToSave the object to save.
	 */
	public static void insert(Appareil objToSave) {
		// Creating a Session and a Transaction
		Session session = Connect_SQLite.getSession();
		Transaction transaction = null;
		try {
			// Begining Transaction
			transaction = session.beginTransaction();

			// Query for the table coordonn�es
			Query<Appareil> query = session.createQuery(
					"from Appareil",
					Appareil.class);
			List<Appareil> list = query.list();
			// Using a Stream, checking if the constraints are fully respected.
			if (list.stream()
					.filter(x -> x.getPartie().equals(objToSave.getPartie()))
					.filter(y -> y.getXy().equals(objToSave.getXy()))
					.count() == 0) {
				session.save(objToSave);
				transaction.commit();
			} else {
				// Little log in case a constraint would not be respected
				System.err.println(
						"Couldn't run insert : UNIQUE constraint failed (x, y)");
			}

		} catch (HibernateException e) {
			System.err
					.println("Unable to run insert stmt in table coordonn�es");
			if (transaction != null)
				transaction.rollback();
		} finally {
			// Closing the session
			session.close();
		}
	}

}

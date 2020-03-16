package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.martin.Connect_SQLite;
import com.martin.Main;
import com.martin.Partie;
import com.martin.model.Coordonn�es;
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

@DatabaseTable(tableName = "appareils")
public class Appareil extends ImageView {

	@DatabaseField(generatedId = true, canBeNull = false, unique = true)
	protected int idAppareil;

	@DatabaseField(foreign = true, foreignColumnName = "idPartie", uniqueCombo = true)
	protected Partie partie;

	@DatabaseField
	protected TypeAppareil type;
	@DatabaseField
	protected Direction direction;
	@DatabaseField
	protected NiveauAppareil niveau;
	@DatabaseField(foreign = true, foreignColumnName = "idCoordonn�es", uniqueCombo = true)
	protected Coordonn�es xy;

	protected Comportement comportement = new Comportement_Aucun();
	protected Sorties sorties = new Sorties_Aucune();
	protected Entr�es entr�es = new Entr�es_Aucune();

	protected JeuContr�le controller;

	protected static int �lectricit� = 5;
	protected Direction pointerExit;
	protected ArrayList<Direction> pointersEnters;

	protected Dashboard dashboard = new Dashboard();

	public Appareil() {
	}

	/**
	 * @author Martin 26 janv. 2020 | 11:30:04
	 * 
	 *         <b> constructor Appareil</b>
	 *         <p>
	 *         Creates a device.
	 *         </p>
	 * 
	 *         Args :
	 * @param xy         the coordinate or this device
	 * @param type       the kind of the device
	 * @param direction  the rotate of this device
	 * @param niveau     the level of this device
	 * @param controller the game controller
	 * 
	 */
	protected Appareil(Coordonn�es xy, TypeAppareil type, Direction direction,
			NiveauAppareil niveau,
			JeuContr�le controller) throws FileNotFoundException {
		super(new LocatedImage(niveau.getURL() + type.getURL()));

		this.type = type;
		this.direction = direction;
		this.niveau = niveau;
		this.controller = controller;
		if (controller != null)
			this.partie = controller.getPartieEnCours();

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

		try {
			if (Connect_SQLite
					.getCoordonn�esDao()
					.queryBuilder()
					.where()
					.eq("x", xy.getX())
					.and()
					.eq("y", xy.getY())
					.query()
					.size() == 0)
				Connect_SQLite.getCoordonn�esDao().create(xy);

			this.xy = Connect_SQLite.getCoordonn�esDao().queryBuilder().where()
					.eq("x", xy.getX()).and()
					.eq("y", xy.getY()).query().get(0);
			this.save();
		} catch (SQLException e) {
			e.printStackTrace();

		}
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
						new Coordonn�es(xy.getX() + pointerExit.getxPlus(),
								xy.getY() + pointerExit.getyPlus()))
				.getXy().isNearFrom(xy)) {

			for (int i = 0; i < pointersEnters.size(); i++) {
				if (pointerExit.equals(controller
						.getPartieEnCours().getAppareil(new Coordonn�es(
								xy.getX() + pointerExit.getxPlus(),
								xy.getY() + pointerExit.getyPlus()))
						.getPointerEnter().get(i)))

					comportement.action(resATraiter);
			}
		}
	}

	public void save() {
		try {
			Connect_SQLite.getAppareilDao().update(this);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	/**
	 * This method resets the database at the coordinates, and do the
	 * necessary to destruct properly this device.
	 * 
	 */
	public void destruction() {
		try {
			Connect_SQLite.getAppareilDao().updateId(
					new Appareil_Sol(xy, Direction.UP, NiveauAppareil.NIVEAU_1,
							controller),
					idAppareil);
		} catch (Exception e) {

		}
	}

	public Appareil toInstance(JeuContr�le controller)
			throws NullPointerException {
		try {
			return this.getType().getClasse()
					.getConstructor(Coordonn�es.class, Direction.class,
							NiveauAppareil.class, JeuContr�le.class)
					.newInstance(this.xy, this.direction, this.niveau,
							controller);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// GETTERS, THEN SETTERS
	public int getID() {
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
	public Coordonn�es getXy() {
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
	public void setXy(Coordonn�es xy) {
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

}

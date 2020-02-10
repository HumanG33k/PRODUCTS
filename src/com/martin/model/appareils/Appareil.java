package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.Main;
import com.martin.model.Coordonn�es;
import com.martin.model.LocatedImage;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement;
import com.martin.model.appareils.comportement.Comportement_Aucun;
import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Entr�es_Aucune;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.model.appareils.orientation.Sorties_Aucune;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.AppareilsContr�le;
import com.martin.view.JeuContr�le;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

public abstract class Appareil extends ImageView{
	
	// FAIRE les m�thodes initializeData()
	// FAIRE les m�thodes getPrix()
	// FAIRE les m�thodes destructions()
	// FAIRE les listes publiques statiques de coordonn�es de r�f�rencement des appareils
	
	protected TypeAppareil type;
	protected Direction direction;
	protected NiveauAppareil niveau;
	protected Coordonn�es xy;
	
	protected Comportement comportement = new Comportement_Aucun();
	protected Sorties sorties = new Sorties_Aucune();
	protected Entr�es entr�es = new Entr�es_Aucune();
	
	protected JeuContr�le controller;
	
	protected static int �lectricit� = 5;
	protected Direction pointerExit;
	protected Direction pointerEnter;
	
	
	/**
	 * @author Martin
	 * 26 janv. 2020 | 11:30:04
	 * 
	 * <b> constructor Appareil</b>
	 * <p>Creates a device.</p>
	 * 
	 * Args :
	 * @param xy the coordinate or this device
	 * @param type the kind of the device
	 * @param direction the rotate of this device
	 * @param niveau the level of this device
	 * @param controller the game controller
	 * 
	*/
	protected Appareil(Coordonn�es xy, TypeAppareil type, Direction direction, NiveauAppareil niveau,
			JeuContr�le controller) throws FileNotFoundException {
		super(new LocatedImage(niveau.getURL()+type.getURL()));
		
		this.xy = xy;
		this.type = type;
		this.direction = direction;
		this.niveau = niveau;
		this.controller = controller;
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					//Charger et ouvrir la bo�te de dialogue demandant l'action � effectuer
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("view/Appareil.fxml"));
					
					Dialog<TypeAppareil> dialog;
					DialogPane dialogPane;
					
					dialogPane = (DialogPane) loader.load();
					dialog = new Dialog<TypeAppareil>();
					dialog.setTitle("S�lection d'appareil - PRODUCTS.");
					dialog.setDialogPane(dialogPane);
					dialog.initOwner(Main.stage);
					dialog.initModality(Modality.NONE);
					
					AppareilsContr�le controller = loader.getController();
					controller.setMainApp(xy.getX(), xy.getY());
					
					dialog.showAndWait();
					
				}catch(IOException e) {
					/*Si une erreur est survenue lors du chargement de la fen�tre,
					 afficher le message plus la raison donn�e par Java.*/
					System.out.println("ERREUR dans Appareil.java entre les lignes 59 et 79 excluses."
							+ "Raison :\n"+e.getMessage());
				}
			}
		});
		try {
			Connect_SQLite.getInstance().createStatement()
			.executeUpdate("UPDATE appareils SET '"+(xy.getX()+1)+"' = "
					+ "'"+type.toString()+"*"+niveau.toString()+"|"+direction.toString()+"' WHERE id = "+(xy.getY()+1));
		} catch (SQLException e) {
			controller.setReport("L'appareil n'a pas pu �tre enregistr� dans la base de donn�es.", Color.DARKRED);
			
		}
	}
	/**
	 * <h1>action</h1>
	 * <p>This method do the action of the device. It calls the defined behaviour.</p>
	 * @param resATraiter the resource who will be used by this device
	 */
	public void action(Ressource resATraiter) throws NegativeArgentException{
		if(this.controller.getGrilleAppareils(new Coordonn�es(xy.getX()+pointerExit.getxPlus(), 
				xy.getY()+pointerExit.getyPlus())).getXy().isNearFrom(xy)){
			
			if(pointerExit.equals(controller.getGrilleAppareils(new Coordonn�es(xy.getX()+pointerExit.getxPlus()
					, xy.getY()+pointerExit.getyPlus())).getPointerEnter()) || this 
					instanceof Appareil_Vendeur)
				comportement.action(resATraiter);
		}
	}
	
	/**
	 * <h1>destruction</h1>
	 * <p>This methode resets the database at the coordinates, and do the necessary to destruct properly this device</p>
	 * 
	*/
	public void destruction(){
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils SET '"+(xy.getX()+1)+"' = \"SOL*NIVEAU_1|UP\" WHERE id = "+(xy.getY()+1)+";");
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(xy.getX()+1)+"' = \"NONE\" WHERE id = "+(xy.getY()+1)+";");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//GETTERS, THEN SETTERS
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
	public Direction getPointerEnter() {
		return pointerEnter;
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
	 * @param �lectricit� the �lectricit� to set
	 */
	public static void set�lectricit�(int �lectricit�) {
		Appareil.�lectricit� = �lectricit�;
	}
	
	
	
}

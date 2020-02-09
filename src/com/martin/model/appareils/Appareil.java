package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
import javafx.stage.Modality;

public abstract class Appareil extends ImageView{
	
	// FAIRE les m�thodes initializeData()
	// FAIRE les m�thodes getPrix()
	// FAIRE les m�thodes destructions()
	// FAIRE les listes publiques statiques de coordonn�es de r�f�rencement des appareils
	
	protected ArrayList<Ressource> ressources = new ArrayList<Ressource>();
	
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
	 * <b>Appareil.init</b>
	 * <p>Cr�e un objet Appareil.</p>
	 * 
	 * Args :
	 * @param xy les coordonn�es de l'appareil construit
	 * @param type le type d'appareil
	 * @param direction l'orientation de l'appareil
	 * @param niveau le niveau de l'appareil
	 * @param controller le controller de l'interface de jeu
	 * 
	*/
	protected Appareil(Coordonn�es xy, TypeAppareil type, Direction direction, NiveauAppareil niveau,
			JeuContr�le controller) throws FileNotFoundException {
		super(new LocatedImage(niveau.getURL()+type.getURL()));
		
		//D�finition de l'objet avec les param�tres demand�s
		this.xy = xy;
		this.type = type;
		this.direction = direction;
		this.niveau = niveau;
		this.controller = controller;
		
		//Quand on clique sur l'image
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
			System.out.println(e.getLocalizedMessage());
			
		}
	}
	/**
	 * 
	 * @param resATraiter la ressource � traiter par l'appareil lors de son action
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
	 * @author Martin
	 * 26 janv. 2020 | 11:25:02
	 * 
	 * <b>destruction</b>
	 * <p>Cette m�thode permet d'effacer toutes le donn�es stock�es par les appareils
	 * (base de donn�es, coordonn�es...)</p>
	 * 
	*/
	public void destruction(){
		//FAIRE m�thode destruction
	}
	/**
	 * @return the ressources
	 */
	public ArrayList<Ressource> getRessources() {
		return ressources;
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
	 * @param ressources the ressources to set
	 */
	public void setRessources(ArrayList<Ressource> ressources) {
		this.ressources = ressources;
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

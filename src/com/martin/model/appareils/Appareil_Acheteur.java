package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.martin.Connect_SQLite;
import com.martin.Partie;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Acheteur;
import com.martin.model.appareils.orientation.Entr�es_Aucune;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.view.JeuContr�le;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public class Appareil_Acheteur extends Appareil {
	
	//La ressource distribu�e par l'acheteur
	@DatabaseField(columnName = "Stock 1")
	Ressource resDistribu�e = Ressource.NONE;
	
	private static SimpleIntegerProperty prix;
	public static ArrayList<Coordonn�es> liste = new ArrayList<Coordonn�es>();
	
	public Appareil_Acheteur(Coordonn�es xy, Direction direction, NiveauAppareil niveau,  
			JeuContr�le controller, Partie partie) throws FileNotFoundException {
		
		super(xy, TypeAppareil.ACHETEUR, direction, niveau, controller, partie);
		liste.add(xy);
		
		entr�es = new Entr�es_Aucune();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Center();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Acheteur(xy, niveau, pointerExit.getxPlus(), 
				pointerExit.getyPlus(), controller);
	}
	@Override 
	public void destruction() {
		try {
			Connect_SQLite.getAppareilDao().delete(this);
		} catch (Exception e) {
			controller.setReport("L'appareil n'a pas �t� d�truit correctement.", Color.DARKRED);
		}
	}
	//Retourne la ressource distribu�e
	public Ressource getRessourceDistribu�e() {
		return resDistribu�e;
	}
	//Set la ressource distribu�e dans la base de donn�e et dans la variable locale
	public void setRessourceDistribu�e(Ressource res) throws SQLException{
		
		this.resDistribu�e = res;
		
		((Appareil_Acheteur) comportement).setRessourceDistribu�e(resDistribu�e);
		Connect_SQLite.getAppareilDao().update(this);
	}
	
	public static void initializeData() {
		try {
			prix = new SimpleIntegerProperty(Connect_SQLite.getInstance().createStatement().executeQuery(
					"SELECT * FROM infos").getInt("prixAcheteur"));
		} catch (SQLException e) {
			System.out.println("ERREUR LORS D'UNE TENTATIVE DE R�CUP�RATION DES DONN�ES du "
					+ "prix de l'acheteur"+e.getMessage());
			prix = new SimpleIntegerProperty(999_999_999);
		}
	}
	public static int getPrix() {
		return prix.get();
	}
}
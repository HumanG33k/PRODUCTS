package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Acheteur;
import com.martin.model.appareils.orientation.Entr�es_Aucune;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.view.JeuContr�le;

import javafx.beans.property.SimpleIntegerProperty;

public class Appareil_Acheteur extends Appareil {
	
	//La ressource distribu�e par l'acheteur
	Ressource resDistribu�e = Ressource.NONE;
	
	private static SimpleIntegerProperty prix;
	public static ArrayList<Coordonn�es> liste = new ArrayList<Coordonn�es>();
	
	public Appareil_Acheteur(Coordonn�es xy, NiveauAppareil niveau, Direction direction, 
			JeuContr�le controller) throws FileNotFoundException {
		
		super(xy, TypeAppareil.ACHETEUR, direction, niveau, controller);
		
		ressources = new ArrayList<Ressource>();
		prix = new SimpleIntegerProperty(500);
		liste.add(xy);
		
		entr�es = new Entr�es_Aucune();
		pointerEnter = Direction.NONE;
		sorties = new Sorties_Center();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Acheteur(xy, niveau, pointerExit.getxPlus(), 
				pointerExit.getyPlus(), controller);
	}
	
	@Override 
	public void destruction() {
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(xy.getX()+1)+"' = 'NONE' WHERE id = '"+(
							xy.getY()+1)+"';");
		} catch (Exception e) {
			System.out.println("ERREUR dans Appareil_Acheteur dans la m�thode " + "destruction. Raison :\n"
					+ e.getLocalizedMessage());
		}
	}
	//Retourne la ressource distribu�e
	public Ressource getRessourceDistribu�e() {
		return resDistribu�e;
	}
	//Set la ressource distribu�e dans la base de donn�e et dans la variable locale
	public void setRessourceDistribu�e(Ressource res) throws SQLException{
		
		this.resDistribu�e = res;
		
		
		Connect_SQLite.getInstance().createStatement().executeUpdate(
				"UPDATE appareils_infos SET '"+(xy.getX()+1)+"' = \""+res.toString()+"\" "
						+ "WHERE id = "+(xy.getY()+1)+";");
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
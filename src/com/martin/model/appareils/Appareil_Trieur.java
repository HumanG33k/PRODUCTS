package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.comportement.Comportement_Convoyeur;
import com.martin.model.appareils.orientation.Entr�es_Center;
import com.martin.model.appareils.orientation.Sorties_Aucune;
import com.martin.model.appareils.orientation.Sorties_Center;
import com.martin.model.appareils.orientation.Sorties_Left;
import com.martin.model.appareils.orientation.Sorties_Right;
import com.martin.model.appareils.orientation.Sorties_Up;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

import javafx.beans.property.SimpleIntegerProperty;

public class Appareil_Trieur extends Appareil {
	
	private static SimpleIntegerProperty prix;
	
	private Ressource crit1, crit2;

	public Appareil_Trieur(Coordonn�es xy, NiveauAppareil niveau, Direction direction, JeuContr�le controller) 
			throws FileNotFoundException {
		super(xy, TypeAppareil.TRIEUR, direction, niveau, controller);
		
		prix = new SimpleIntegerProperty(7500);
		
		try {
			String strCrit1, strCrit2;
			ResultSet res = Connect_SQLite.getInstance().prepareStatement(
					"SELECT * FROM appareils_infos WHERE id = "+xy.getY()+";").executeQuery();
			
			for(int i = 0; i < res.getString(""+xy.getX()+"").length(); i++) {
				if(res.getString(""+xy.getX()+"").substring(i, i+1).equals("|")) {
					
					strCrit1 = res.getString(""+xy.getX()+"").substring(0, i);
					strCrit2 = res.getString(""+xy.getX()+"").substring(i+1);
					
					crit1 = Ressource.valueOf(strCrit1);
					crit2 = Ressource.valueOf(strCrit2);
					break;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		entr�es = new Entr�es_Center();
		pointersEnters = entr�es.getPointers(direction);
		sorties = new Sorties_Aucune();
		pointerExit = sorties.getPointer(direction);
		comportement = new Comportement_Convoyeur(xy, niveau, pointerExit.getxPlus(), 
				pointerExit.getyPlus(), controller);
	}

	@Override
	public void action(Ressource[] resATraiter) throws NegativeArgentException {
		
		if(resATraiter[0] == crit1) {
			switch(direction) {
			case UP:
				sorties = new Sorties_Right();
				pointerExit = sorties.getPointer(direction);
				break;
			case RIGHT:
				sorties = new Sorties_Center();
				pointerExit = sorties.getPointer(direction);
				break;
			case DOWN:
				sorties = new Sorties_Left();
				pointerExit = sorties.getPointer(direction);
				break;
			case LEFT:
				sorties = new Sorties_Up();
				pointerExit = sorties.getPointer(direction);
				break;
			default:
				break;
			}
		}else if(resATraiter[0] == crit2) {
			switch(direction) {
			case UP:
				sorties = new Sorties_Left();
				pointerExit = sorties.getPointer(direction);
				break;
			case RIGHT:
				sorties = new Sorties_Up();
				pointerExit = sorties.getPointer(direction);
				break;
			case DOWN:
				sorties = new Sorties_Right();
				pointerExit = sorties.getPointer(direction);
				break;
			case LEFT:
				sorties = new Sorties_Left();
				pointerExit = sorties.getPointer(direction);
				break;
			default:
				break;
			}
		}else {
			switch(direction) {
			case UP:
				sorties = new Sorties_Left();
				pointerExit = sorties.getPointer(direction);
				break;
			case RIGHT:
				sorties = new Sorties_Left();
				pointerExit = sorties.getPointer(direction);
				break;
			case DOWN:
				sorties = new Sorties_Up();
				pointerExit = sorties.getPointer(direction);
				break;
			case LEFT:
				sorties = new Sorties_Right();
				pointerExit = sorties.getPointer(direction);
				break;
			default:
				break;
			}
		}
		
		comportement = new Comportement_Convoyeur(xy, niveau, pointerExit.getxPlus(), 
				pointerExit.getyPlus(), controller);
		comportement.action(resATraiter);
	}
	
	public static void initializeData() {
		try {
			prix = new SimpleIntegerProperty(Connect_SQLite.getInstance().createStatement().executeQuery(
					"SELECT * FROM infos").getInt("prixTrieur"));
		} catch (SQLException e) {
			System.out.println("ERREUR LORS D'UNE TENTATIVE DE R�CUP�RATION DES DONN�ES du "
					+ "prix de l'acheteur"+e.getMessage());
			prix = new SimpleIntegerProperty(999_999_999);
		}
	}
	public static int getPrix() {
		return prix.get();
	}
	
	public void setCrit�re1(Ressource res) {
		this.crit1 = res;
		
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(xy.getX()+1)+"' = \""+(res.toString()+"|"+crit2.toString())+"\" WHERE id = "+(xy.getY()+1)+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setCrit�re2(Ressource res) {
		this.crit2 = res;
		
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(xy.getX()+1)+"' = \""+(crit1.toString()+"|"+res.toString())+"\" WHERE id = "+(xy.getY()+1)+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Ressource getCrit�re1() {
		return crit1;
	}
	public Ressource getCrit�re2() {
		return crit2;
	}
}
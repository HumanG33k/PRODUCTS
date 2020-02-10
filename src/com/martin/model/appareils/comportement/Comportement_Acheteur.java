package com.martin.model.appareils.comportement;

import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

import javafx.scene.paint.Color;

public class Comportement_Acheteur implements Comportement {
	
	private Coordonn�es pointer;
	private NiveauAppareil niveau;
	private JeuContr�le controller;
	
	private Ressource resDistribu�e = Ressource.NONE;
	
	public Comportement_Acheteur(Coordonn�es xy, NiveauAppareil niveau, 
			int xToAdd, int yToAdd, JeuContr�le controller){
		this.niveau = niveau;
		this.controller = controller;
		this.pointer = new Coordonn�es(xy.getX()+xToAdd, xy.getY()+yToAdd);
		
		try {
			resDistribu�e = Ressource.valueOf(Connect_SQLite.getInstance().prepareStatement(
					"SELECT * FROM appareils_infos WHERE id = "+(xy.getX()+1)+";").executeQuery()
					.getString(""+(xy.getY()+1)+""));
		} catch (SQLException e) {
			controller.setReport("Attention : un acheteur n'a pas pu charger la ressource qu'il doit distribuer", Color.ORANGE.darker());
		}
	}
	
	@Override
	public void action(Ressource resATraiter) throws NegativeArgentException{
		for(int niveau = 0; this.niveau.getNiveau() == niveau+1; niveau++) {
			if(controller.getArgentProperty().get() < 5+Appareil.get�lectricit�())
				throw new NegativeArgentException("Le comportement d'un appareil "
						+ "n'a pas pu �tre r�alis� car le solde d'argent n'�tait pas assez important.");
			controller.getGrilleAppareils(pointer).action(resDistribu�e);
			controller.setArgent(5+Appareil.get�lectricit�(), false);
		}
	}	
}
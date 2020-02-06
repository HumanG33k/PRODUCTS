package com.martin.model.appareils.comportement;

import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Comportement_Acheteur implements Comportement {
	
	private Coordonn�es pointer;
	private NiveauAppareil niveau;
	private JeuContr�le controller;
	
	private Ressource resDistribu�e;
	
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
			System.out.println("ERREUR dans Comportement_Acheteur dans la m�thode "
					+ "Comportement_Acheteur. Raison :\n" + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void action(Ressource resATraiter) {
		for(int niveau = 0; this.niveau.getNiveau() == niveau+1; niveau++) {
			try {
				controller.getGrilleAppareils(pointer).action(resDistribu�e);
				controller.setArgent(5+Appareil.get�lectricit�(), false);
			} catch (NegativeArgentException e) {
				System.out.println("ERREUR dans Comportement_Acheteur dans la m�thode action. Raison :\n"
						+ e.getLocalizedMessage());
			}
		}
	}	
}
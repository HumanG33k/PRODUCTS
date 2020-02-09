package com.martin.model.appareils.comportement;

import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Comportement_Vendeur implements Comportement {

	private NiveauAppareil niveau;
	private JeuContr�le controller;
	
	public Comportement_Vendeur(Coordonn�es xy, NiveauAppareil niveau, 
			int xToAdd, int yToAdd, JeuContr�le controller){
		this.niveau = niveau;
		this.controller = controller;
	}
	
	@Override
	public void action(Ressource resATraiter) throws NegativeArgentException{
		for(int i = 0; i < niveau.getNiveau(); i++) {
			if(!resATraiter.equals(Ressource.NONE))
				controller.setArgent(resATraiter.getValue()-Appareil.get�lectricit�(), true);
		}
	}

}

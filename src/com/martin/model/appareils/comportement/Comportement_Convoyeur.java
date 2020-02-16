package com.martin.model.appareils.comportement;

import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;
import com.martin.view.JeuContr�le;

public class Comportement_Convoyeur implements Comportement {

	private Coordonn�es pointer;
	private NiveauAppareil niveau;
	private JeuContr�le controller;
	
	public Comportement_Convoyeur(Coordonn�es xy, NiveauAppareil niveau, 
			int xToAdd, int yToAdd, JeuContr�le controller) {
		this.niveau = niveau;
		this.controller = controller;
		this.pointer = new Coordonn�es(xy.getX()+xToAdd, xy.getY()+yToAdd);
	}

	@Override
	public void action(Ressource[] resATraiter) throws NegativeArgentException {
		for(int niveau = 0; this.niveau.getNiveau() == niveau+1; niveau++) {
			if(controller.getArgentProperty().get() < 5+Appareil.get�lectricit�())
				throw new NegativeArgentException("Le comportement d'un appareil "
						+ "n'a pas pu �tre r�alis� car le solde d'argent n'�tait pas assez important.");
			controller.getGrilleAppareils(pointer).action(resATraiter);
			controller.setArgent(Appareil.get�lectricit�(), false);
		}
	}

}

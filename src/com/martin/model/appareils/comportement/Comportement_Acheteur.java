package com.martin.model.appareils.comportement;

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
	
	private Ressource[] resDistribu�e = {Ressource.NONE};
	
	public Comportement_Acheteur(Coordonn�es xy, NiveauAppareil niveau, 
			int xToAdd, int yToAdd, JeuContr�le controller, int idAppareil){
		this.niveau = niveau;
		this.controller = controller;
		this.pointer = new Coordonn�es(xy.getX()+xToAdd, xy.getY()+yToAdd);
		
		// FixMe : charger la ressource
		resDistribu�e[0] = Ressource.OR;
	}
	
	@Override
	public void action(Ressource[] resATraiter) throws NegativeArgentException{
		for(int niveau = 0; this.niveau.getNiveau() == niveau+1; niveau++) {
			if(controller.getArgent() < 5+Appareil.get�lectricit�())
				throw new NegativeArgentException("Le comportement d'un acheteur "
						+ "n'a pas pu �tre r�alis� car le solde d'argent n'�tait pas assez important.");
			controller.getGrilleAppareils(pointer).action(resDistribu�e);
			if(!resDistribu�e[0].equals(Ressource.NONE))
				controller.setArgent(5+Appareil.get�lectricit�(), false);
		}
	}
	
	/**
	 * <h1>setProduit</h1>
	 * <p>Sets the products to the new value, after checking if it is a valid resource.</p>
	 * @param produit the resource to set
	 */
	public void setProduit(Ressource resDistribu�e) {
		switch(resDistribu�e) {
		case FER: 
		case OR:
		case CUIVRE:
		case ARGENT:
		case DIAMANT:
		case ALUMINIUM:
			this.resDistribu�e[0] = resDistribu�e;
		default:
			break;
		}
			
	}
}
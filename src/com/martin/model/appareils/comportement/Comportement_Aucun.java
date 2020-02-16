package com.martin.model.appareils.comportement;

import com.martin.model.Ressource;
import com.martin.model.exceptions.NegativeArgentException;

public class Comportement_Aucun implements Comportement {
	
	/**
	 * @author Martin
	 * 27 janv. 2020 | 12:09:54
	 * 
	 * <b>action</b>
	 * <p>R�alise l'action de l'appareil</p>
	 * 
	 * Args :
	 * @param resATraiter la ressource � traiter par l'appareil
	 * 
	*/
	@Override
	public void action(Ressource[] resATraiter) throws NegativeArgentException{
		/*Cette m�thode ne d�finit aucun comportement mais est n�cessaire
		 * pour les appareils comme les sols.*/

	}

}

package com.martin.model.appareils.comportement;

import com.martin.model.Stock;
import com.martin.model.exceptions.NegativeArgentException;

public class Comportement_Aucun implements Comportement {

	/**
	 * <b>action</b>
	 * <p>
	 * R�alise l'action de l'appareil
	 * </p>
	 * 
	 * @param resATraiter la ressource � traiter par l'appareil
	 * 
	 */
	@Override
	public void action(Stock resATraiter) throws NegativeArgentException {
		/*
		 * Cette m�thode ne d�finit aucun comportement mais est n�cessaire
		 * pour les appareils comme les sols.
		 */

	}

}

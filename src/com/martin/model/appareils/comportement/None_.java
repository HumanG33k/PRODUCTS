package com.martin.model.appareils.comportement;

import com.martin.model.Stock;
import com.martin.model.exceptions.MoneyException;

public class None_ implements Behaviour {

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
	public void action(Stock resATraiter) throws MoneyException {
		/*
		 * Cette m�thode ne d�finit aucun comportement mais est n�cessaire
		 * pour les appareils comme les sols.
		 */

	}

}

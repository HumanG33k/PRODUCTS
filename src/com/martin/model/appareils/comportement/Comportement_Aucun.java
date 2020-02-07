package com.martin.model.appareils.comportement;

import com.martin.model.Ressource;
import com.martin.model.appareils.Direction;
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
	public void action(Ressource resATraiter) throws NegativeArgentException{
		/*Cette m�thode ne d�finit aucun comportement mais est n�cessaire
		 * pour les appareils comme les sols.*/

	}

	/**
	 * @author Martin
	 * 27 janv. 2020 | 12:07:48
	 * 
	 * <b>checkRotation</b>
	 * <p>Met � jour les entr�es, sorties et le pointeur en fonction de la rotation actuel</p>
	 * 
	 * Args :
	 * @param direction la rotation actuelle
	 * 
	*/
	//REMPLACER cette m�thode dans le futur interface Orientation
	public void checkRotation(Direction direction) {
		//Ici, pareil, pas besoin de d�finir une rotation puisque la case ne fait rien
		
	}

}

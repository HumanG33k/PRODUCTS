package com.martin.model;

/**
 * @author Heywang
 * 24/01/2020
 * 
 * R�le : objet regroupant des valeurs x et y. Utilis� par les appareils.
 *
 */
public class Coordonn�es {
	
	/**
	 * @serialField x and y*/
	private int x, y;
	
	/**
	 * @author Heywang
	 * 24/01/2020
	 * 
	 * @category constructor
	 * 
	 * R�le : Cr�e un objet Coordonn�es*/
	public Coordonn�es(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @author Heywang
	 * 24/01/2020
	 * 
	 * @category getter
	 * 
	 * R�le : @return le champ x repr�sent� par cet objet Coordonn�es*/
	public int getX() {
		return x;
	}
	/**
	 * @author Heywang
	 * 
	 * @category getter
	 * @return le champ y repr�sent� par cet objet Coordonn�es
	 */
	public int getY() {
		return y;
	}
	/**
	 * 
	 * @param tailleMax
	 * @return si les coordonn�es se situent toutes les deux entre 0 et la taille pass�e en param�tre
	 */
	public boolean isInGrid(int tailleMax) {
		if(x < 0 || y < 0 || x > tailleMax || y > tailleMax) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param coordonn�es
	 * @return si l'objet Coordonn�es en param�tre et cet objet lui-m�me se situe pr�s l'un de l'autre
	 */
	public boolean isNearFrom(Coordonn�es coordonn�es){
		if(this.x == coordonn�es.getX()+1 || this.x == coordonn�es.getX()-1){
			return true;
		}
		if(this.y == coordonn�es.getY()+1 || this.x == coordonn�es.getY()-1){
			return true;
		}
		return false;
	}
}

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
	 * R�le : @return x and y fields*/
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean isInGrid(int tailleMax) {
		if(x < 0 || y < 0 || x > tailleMax || y > tailleMax) {
			return false;
		}
		return true;
	}
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

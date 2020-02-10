package com.martin.model;

public class Paquet {

	private Ressource ressource;
	private int quantit�;
	
	public Paquet() {
		this.ressource = Ressource.NONE;
		this.quantit� = 0;
	}
	
	/**
	 * <h1>constructor Paquet</h1>
	 * 
	 * @param res a resource 
	 * @param qt� its quantity
	 */
	public Paquet(Ressource res, int qt�) {
		this.ressource = res;
		this.quantit� = qt�;
	}
	
	/**
	 * 
	 * @return resource the resource
	 */
	public Ressource getRessource() {
		return ressource;
	}
	/**
	 * 
	 * @param res the resource to set
	 */
	public void setRessource(Ressource res) {
		this.ressource = res;
	}
	
	/**
	 * 
	 * @return quantit� the quantity
	 */
	public int getQuantit�() {
		return quantit�;
	}
	/**
	 * 
	 * @param quantit� the quantity to change
	 */
	public void setQuantit�(int quantit�) {
		this.quantit� = quantit�;
	}

}

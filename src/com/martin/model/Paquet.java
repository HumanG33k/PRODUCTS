package com.martin.model;

public class Paquet {

	private Ressource ressource;
	private int quantit�;
	
	public Paquet() {
		this.ressource = Ressource.NONE;
		this.quantit� = 0;
	}
	
	public Paquet(Ressource res, int qt�) {
		this.ressource = res;
		this.quantit� = qt�;
	}
	
	public Ressource getRessource() {
		return ressource;
	}
	public void setRessource(Ressource res) {
		this.ressource = res;
	}
	
	public int getQuantit�() {
		return quantit�;
	}
	public void setQuantit�(int quantit�) {
		this.quantit� = quantit�;
	}

}

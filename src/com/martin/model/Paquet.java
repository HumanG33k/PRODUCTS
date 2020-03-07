package com.martin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.martin.model.appareils.Appareil;

@DatabaseTable(tableName = "paquets")
public class Paquet{
	
	
	@DatabaseField(generatedId = true)
	private int idPaquet;
	
	@DatabaseField
	private Ressource ressource;
	
	@DatabaseField
	private int quantit�;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignColumnName = "idAppareil")
	private Appareil appareil;
	
	public Paquet(Ressource ressource,int quantit�, Appareil appareil) {
		this.ressource = ressource;
		this.quantit� = quantit�;
	}
	
	public Ressource getRessource() {
		return ressource;
	}
	public int getQuantit�() {
		return quantit�;
	}
	public Appareil getAppareil() {
		return appareil;
	}
}

package com.martin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "paquets")
public class Paquet {

	@DatabaseField(generatedId = true)
	private int idPaquet;

	@DatabaseField
	private Ressource ressource;

	@DatabaseField
	private int quantit�;

	public Paquet(Ressource ressource, int quantit�) {
		this.ressource = ressource;
		this.quantit� = quantit�;
	}

	public Ressource getRessource() {
		return ressource;
	}

	public int getQuantit�() {
		return quantit�;
	}
}

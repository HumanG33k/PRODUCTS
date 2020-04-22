package com.martin.model.appareils;

public enum Level {
	NIVEAU_1("images/machines niveau 1/", 1),
	NIVEAU_2("images/machines niveau 2/", 2),
	NIVEAU_3("images/machines niveau 3/", 3);
	
	String url;
	int niveau;
	
	Level(String url, int niveau) {
		this.url = url;
		this.niveau = niveau;
	}
	
	public String getURL() {
		return url;
	}
	public int getNiveau(){
		return niveau;
	}
	public Level getLevelSup(Level level) {
		if(level == NIVEAU_1)
			return NIVEAU_2;
		if(level == NIVEAU_2)
			return NIVEAU_3;
		else
			return NIVEAU_1;
	}
}
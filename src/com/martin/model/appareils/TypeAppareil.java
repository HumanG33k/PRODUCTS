package com.martin.model.appareils;

public enum TypeAppareil {
	
	/*
	 * La liste des appareils actuellement en jeu. 
	 * Chacun contient plein d'informations n�cessaires, comme le nom, la classe qui le g�re...
	 * L'ouverture de la fen�tre de Appareil.fxml se sert de cette enum pour 
	 * afficher les nombres de appareils notamment.
	 * Si on en enl�ve un, il dispara�t de la fen�tre...
	 * 
	 * Note : le sol ne devant pas s'afficher, il doit ABSOLUMENT rester en derni�re position.
	*/
	
	ACHETEUR("Acheteur", "Acheteur.png", "Ach�te les ressources de base.", Appareil_Acheteur.class),
	VENDEUR("Vendeur", "Vendeur.png", "Vend les ressources et objets \nlui parvenant.", Appareil_Vendeur.class),
	CONVOYEUR("Convoyeur", "Convoyeur.png", "Transporte les ressources sur \nla case du bas.", Appareil_Convoyeur.class),
	CONVOYEUR_DROITE("Convoyeur droite", "Convoyeur_Droite.png", "Transporte les ressources sur \nla case de droite.", Appareil_Convoyeur_Droite.class),
	CONVOYEUR_GAUCHE("Convoyeur gauche", "Convoyeur_Gauche.png", "Transporte les ressources sur \nla case de gauche.", Appareil_Convoyeur_Gauche.class),
	FOUR("Four", "Four.png", "Fond toutes les ressources en lingots, \nsauf le diamant.", Appareil_Four.class),
	PRESSE("Presse", "Presse.png", "Transforme toutes les ressources en plaques, \nsauf le diamant.", Appareil_Presse.class),
	PRESSE_FIL("Presse � fil", "Appareil_Fil.png", "Transforme les ressources en fil, \nsauf le diamant.", Appareil_Presse_Fil.class),
	ASSEMBLEUR("Assembleur", "Assembleur.png", "Assemble les ressources pour les \ntransformer en produits.", Appareil_Assembleur.class),
	TRIEUR("Trieur", "Trieur.png", "Trie les ressources selon un sch�ma pr�cis \nde votre d�cision.", Appareil_Trieur.class),
	
	SOL("Sol", "Sol.png", "Le sol � nu sans appareil. Il ne fait rien.", Appareil_Sol.class);
	
	//Le nom, l'url, la description et la classe
	String nom;
	String url;
	String desc;
	Class<? extends Appareil> classe;
	
	//Un seul et unique constructeur
	TypeAppareil(String nom, String url, String desc, Class<? extends Appareil> classe){
		this.nom = nom;
		this.url = url;
		this.desc = desc;
		this.classe = classe;
	}
	
	//Les getters
	public String getNom() {
		return this.nom;
	}
	public String getURL() {
		return this.url;
	}
	public String getDescription() {
		return this.desc;
	}
	public Class<? extends Appareil> getClasse(){
		return classe;
	}
}

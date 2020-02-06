package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.Stats;
import com.martin.view.JeuContr�le;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;

public class Appareil_Convoyeur_Gauche extends Appareil {
	
	int X = 0, Y = 0;
	
	private static SimpleIntegerProperty prix;

	public Appareil_Convoyeur_Gauche(int x, int y, String rotate) {
		super(x, y, rotate);
		prix.bind(Appareil_Convoyeur.getPrix());
		
		checkRotation(rotate);
	}
	
	public void checkRotation(String rotate) {
		entr�es.clear();
		sorties.clear();
		switch(rotate) {
		case "000":
			entr�es.add(Direction.DOWN);
			sorties.add(Direction.LEFT);
			break;
		case "090":
			entr�es.add(Direction.LEFT);
			sorties.add(Direction.UP);
			break;
		case "180":
			entr�es.add(Direction.UP);
			sorties.add(Direction.RIGHT);
			break;
		case "270":
			entr�es.add(Direction.RIGHT);
			sorties.add(Direction.DOWN);
			break;
		}
	}

	@Override
	public void action() {
		for(int k = 0; k < niveau; k++) {
		for(int i = 0; i < sorties.size(); i++) {
			
			
			switch(rotate) {
			case "000":
				X = this.x-1;
				Y = this.y;
				break;
			case "090":
				X = this.x;
				Y = this.y+1;
				break;
			case "180":
				X = this.x+1;
				Y = this.y;
				break;
			case "270":
				X = this.x;
				Y = this.y-1;
				break;
			}
			if(X>-1 && Y>-1 && X<Stats.largeurGrille && Y<Stats.longueurGrille) {
				for(int j = 0; j < JeuContr�le.images[X][Y].getAppareil().getEntr�esList().size(); j++) {
					
					Platform.runLater(new Runnable() {
						public void run() {
							if(JeuContr�le.images[x][y].getStage().isShowing()) {
								try {
									JeuContr�le.images[x][y].getControler().setLabelSortie(ressources.get(0).getNom());
									JeuContr�le.images[x][y].getControler().setImgSortie(ressources.get(0).getURL());
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
							else if(JeuContr�le.images[X][Y].getStage().isShowing()) {
								try {
									JeuContr�le.images[X][Y].getControler().setLabelEntr�e(ressources.get(0).getNom());
									JeuContr�le.images[X][Y].getControler().setImgEntr�e(ressources.get(0).getURL());
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
							ressources.remove(0);
						}
					});
					
					if(sorties.get(i) == JeuContr�le.images[X][Y].getAppareil().getEntr�esList().get(j) && ressources.size() > 0) {
						JeuContr�le.images[X][Y].getAppareil().getRessources().add(ressources.get(0));
						
						JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.�lectricit�);
					}
				}
			}
		}
		
		}
	}

	@Override 
	public void destroy() {}
	
	public static void initializeData() {
		prix = new SimpleIntegerProperty();
		prix.bind(Appareil_Convoyeur.prix);
	}
	public static int getPrix() {
		return prix.get();
	}
	

}

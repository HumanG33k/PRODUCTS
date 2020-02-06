package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.Stats;
import com.martin.model.Ressource;
import com.martin.view.JeuContr�le;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;

public class Appareil_Trieur extends Appareil {
	
	int X = 0, Y = 0;
	private Ressource crit1 = Ressource.NONE, crit2 = Ressource.NONE;
	private Direction sortieActu;
	
	private static SimpleIntegerProperty prix;

	public Appareil_Trieur(int x, int y, NiveauAppareil niveau, Direction direction, JeuContr�le controller) {
		super(x, y, TypeAppareil.TRIEUR, direction, niveau, controller);
		
		prix = new SimpleIntegerProperty(7500);
		
		try {
			String strCrit1, strCrit2;
			ResultSet res = Connect_SQLite.getInstance().prepareStatement(
					"SELECT * FROM appareils_infos WHERE id = "+y+";").executeQuery();
			
			for(int i = 0; i < res.getString(""+x+"").length(); i++) {
				if(res.getString(""+x+"").substring(i, i+1).equals("|")) {
					
					strCrit1 = res.getString(""+x+"").substring(0, i);
					strCrit2 = res.getString(""+x+"").substring(i+1);
					
					crit1 = Ressource.valueOf(strCrit1);
					crit2 = Ressource.valueOf(strCrit2);
					break;
				}
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		checkRotation(direction);
	}
	public void checkRotation(Direction direction) {
		entr�es.clear();
		
		switch(direction) {
		case UP:
			entr�es.add(Direction.DOWN);
			
			sorties.add(Direction.UP);
			sorties.add(Direction.RIGHT);
			sorties.add(Direction.LEFT);
			break;
		case RIGHT:
			entr�es.add(Direction.RIGHT);
			
			sorties.add(Direction.UP);
			sorties.add(Direction.DOWN);
			sorties.add(Direction.LEFT);
			
			break;
		case DOWN:
			entr�es.add(Direction.UP);
			
			sorties.add(Direction.DOWN);
			sorties.add(Direction.RIGHT);
			sorties.add(Direction.LEFT);

			break;
		case LEFT:
			entr�es.add(Direction.LEFT);
			
			sorties.add(Direction.UP);
			sorties.add(Direction.RIGHT);
			sorties.add(Direction.DOWN);
			
			break;
		}
	}

	@Override
	public void action() {
		for(int i = 0; i < niveau; i++) {
		if(ressources.get(0) == crit1) {
			switch(rotate) {
			case "000":
				X = this.x-1;
				Y = this.y;
				sortieActu = Direction.RIGHT;
				break;
			case "090":
				X = this.x;
				Y = this.y+1;
				sortieActu = Direction.DOWN;
				break;
			case "180":
				X = this.x+1;
				Y = this.y;
				sortieActu = Direction.LEFT;
				break;
			case "270":
				X = this.x;
				Y = this.y-1;
				sortieActu = Direction.UP;
				break;
			}
		}else if(ressources.get(0) == crit2) {
			switch(rotate) {
			case "000":
				X = this.x+1;
				Y = this.y;
				sortieActu = Direction.LEFT;
				break;
			case "090":
				X = this.x;
				Y = this.y-1;
				sortieActu = Direction.UP;
				break;
			case "180":
				X = this.x-1;
				Y = this.y;
				sortieActu = Direction.RIGHT;
				break;
			case "270":
				X = this.x;
				Y = this.y+1;
				sortieActu = Direction.DOWN;
				break;
			}
		}else {
			switch(rotate) {
			case "000":
				X = this.x;
				Y = this.y+1;
				sortieActu = Direction.DOWN;
				break;
			case "090":
				X = this.x-1;
				Y = this.y;
				sortieActu = Direction.RIGHT;
				break;
			case "180":
				X = this.x;
				Y = this.y-1;
				sortieActu = Direction.UP;
				break;
			case "270":
				X = this.x+1;
				Y = this.y;
				sortieActu = Direction.LEFT;
				break;
			}
		}
		if(X>-1 && Y>-1 && X<Stats.largeurGrille && Y<Stats.longueurGrille) {
			for(int j = 0; j < JeuContr�le.images[X][Y].getAppareil().getEntr�esList().size(); j++) {
				if((sortieActu == JeuContr�le.images[X][Y].getAppareil().getEntr�esList().get(j)) && (ressources.size() > 0)) {
					
					
					JeuContr�le.images[X][Y].getAppareil().ressources.add(ressources.get(0));
					
					Platform.runLater(new Runnable() {
						public void run() {
							if(JeuContr�le.images[x][y].getStage().isShowing()) {
								try {
									JeuContr�le.images[x][y].getControler().setLabelSortie(ressources.get(0).getNom());
									JeuContr�le.images[x][y].getControler().setImgSortie(ressources.get(0).getURL());
									ressources.remove(0);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
							else if(JeuContr�le.images[X][Y].getStage().isShowing()) {
								try {
									JeuContr�le.images[X][Y].getControler().setLabelEntr�e(ressources.get(0).getNom());
									JeuContr�le.images[X][Y].getControler().setImgEntr�e(ressources.get(0).getURL());
									ressources.remove(0);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
							ressources.remove(0);
						}
					});
					
					JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.�lectricit�);
				}
			}
		}
		}
	}
	
	
	public void destroy() {
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(x+1)+"' = 'NONE' WHERE id = '"+(y+1)+"';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeData() {
		try {
			prix = new SimpleIntegerProperty(Connect_SQLite.getInstance().createStatement().executeQuery(
					"SELECT * FROM infos").getInt("prixTrieur"));
		} catch (SQLException e) {
			System.out.println("ERREUR LORS D'UNE TENTATIVE DE R�CUP�RATION DES DONN�ES du "
					+ "prix de l'acheteur"+e.getMessage());
			prix = new SimpleIntegerProperty(999_999_999);
		}
	}
	public static int getPrix() {
		return prix.get();
	}
	
	public void setCrit�re1(Ressource res) {
		this.crit1 = res;
		
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(x+1)+"' = \""+(res.toString()+"|"+crit2.toString())+"\" WHERE id = "+(y+1)+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setCrit�re2(Ressource res) {
		this.crit2 = res;
		
		try {
			Connect_SQLite.getInstance().createStatement().executeUpdate(
					"UPDATE appareils_infos SET '"+(x+1)+"' = \""+(crit1.toString()+"|"+res.toString())+"\" WHERE id = "+(y+1)+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Ressource getCrit�re1() {
		return crit1;
	}
	public Ressource getCrit�re2() {
		return crit2;
	}
}
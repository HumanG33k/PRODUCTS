package com.martin;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.martin.model.Coordonn�es;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.Appareil_Sol;
import com.martin.model.appareils.Direction;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.view.JeuContr�le;

@DatabaseTable(tableName = "parties")
public class Partie {

	@DatabaseField(generatedId = true, unique = true, columnName = "idPartie")
	private int idPartie;
	
	@DatabaseField(canBeNull = false)
	private String nom;
	
	@DatabaseField(canBeNull = false)
	private String lastView;
	
	@DatabaseField(canBeNull = false, defaultValue = "1250")
	private long argent;
	
	@DatabaseField(canBeNull = false, defaultValue = "3")
	private int tailleGrille;
	
	private List<? extends Appareil> listAppareils;
	
	public Partie() {}

	public Partie(String nom) throws SQLException {
		this.nom = nom;
		this.lastView = LocalDateTime.now().toString();
		this.tailleGrille = 3;
		this.argent = 1250;
		
		Connect_SQLite.getPartieDao().createIfNotExists(this);
		
		for(int x = 0; x < tailleGrille; x++) {
			for (int y = 0; y < tailleGrille; y++) {
				try {
					Connect_SQLite.getAppareilDao().createIfNotExists(new Appareil_Sol(new Coordonn�es(x, y), Direction.UP, 
							NiveauAppareil.NIVEAU_1, JeuContr�le.get(), this));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					
				}
			}
		}
		listAppareils = Connect_SQLite.getAppareilDao().queryBuilder().where().eq("partie_idPartie", idPartie)
				.query();
		
		
		this.save();
	}
	public Partie(String nom, String lastView) throws SQLException {
		this.nom = nom;
		this.lastView = lastView;
		this.tailleGrille = 3;
		
		Connect_SQLite.getPartieDao().createIfNotExists(this);
		for(int x = 0; x < tailleGrille; x++) {
			for (int y = 0; y < tailleGrille; y++) {
				try {
					Connect_SQLite.getAppareilDao().createIfNotExists(new Appareil_Sol(new Coordonn�es(x, y), Direction.UP, 
							NiveauAppareil.NIVEAU_1, JeuContr�le.get(), this));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					
				}
			}
		}
		listAppareils = Connect_SQLite.getAppareilDao().queryBuilder().where().eq("partie_idPartie", idPartie)
				.query();
		
		
		this.save();
	}
	
	/**
	 * <h1>rename</h1>
	 * <p>Renames this object</p>
	 * @param newName the new name
	 */
	public void rename(String newName) {
		this.nom = newName;
	}
	/**
	 * <h1>save</h1>
	 * <p>Saves this object in the database properly and all its device</p>
	 */
	public void save() {
		try {
			Connect_SQLite.getPartieDao().createOrUpdate(this);
			for(int i = 0; i < this.getAppareils().size(); i++) {
				this.getAppareils().get(i).save();
			}
		} catch (SQLException e) {
			System.err.println("La partie n'a pas pu �tre sauvegard�e.");
			
		}
	}
	public void delete() {
		// Todo : delete method
	}
	
	public int getID() {
		return idPartie;
	}
	public String getNom() {
		return nom;
	}
	public long getArgent() {
		return argent;
	}
	public int getTailleGrille() {
		return tailleGrille;
	}
	
	public List<? extends Appareil> getAppareils(){
		try {
			listAppareils = Connect_SQLite.getAppareilDao().queryBuilder().where().eq("partie_idPartie", idPartie)
					.query();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return listAppareils;
	}
	public Appareil getAppareilsWithCoordinates(Coordonn�es xy) throws NullPointerException{
		try {
			return Connect_SQLite.getAppareilDao().queryBuilder().where()
					.eq("partie_idPartie", this.idPartie).and()
					.eq("xy_idCoordonn�es", xy.getID())
					.queryForFirst().toInstance();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public LocalDateTime getLastView() {
		return LocalDateTime.parse(lastView);
	}
	
	public boolean setArgent(long argent, boolean increase){
		if(increase) {
			this.argent += argent;
			return true;
		}
		else {
			if(this.argent > argent) {
				this.argent -= argent;
				return true;
			}
			else {
				return false;
			}
		}
	}
	public boolean setAppareil(Appareil appareil, int idOldAppareil, boolean ignoreCost) {
		try {
			Connect_SQLite.getAppareilDao().updateId(appareil, idOldAppareil);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

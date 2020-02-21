package com.martin.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * <h1>class Coordonn�es</h1>
 * <p>Object who defines x and y coordinates.<br/>Mainly used by devices.</p>
 *
 */
@DatabaseTable(tableName = "coordonn�es")
public class Coordonn�es {
	
	@DatabaseField(generatedId = true, unique = true, columnName = "idCoordonn�es")
	private int idCoordonn�es;
	
	@DatabaseField(uniqueCombo = true)
	private int x, y;
	
	/**
	 * <h1>constructor Coordonn�es</h1>
	 * <p>Creates coordinates.</p>
	 * 
	 * @param x x coordinates of the object
	 * @param y y coordinates of the object
	 * */
	public Coordonn�es(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Coordonn�es() {}
	
	/**
	 * <h1>getX</h1>
	 * @return the field x of this object
	 */
	public int getX() {
		return x;
	}
	/**
	 * <h1>getY</h1>
	 * @return the field y of this object
	 */
	public int getY() {
		return y;
	}
	/**
	 * <h1>isInGrid</h1>
	 * @param tailleMax the current size of the grid.
	 * @return <ul>
	 * 			<li>true if the coordinate is in the grid
	 * 			<li>false if the coordinate isn't in grid
	 * 		   </ul>
	 */
	public boolean isInGrid(int tailleMax) {
		if(x < 0 || y < 0 || x > tailleMax || y > tailleMax) {
			return false;
		}
		return true;
	}
	/**
	 * <h1>isNearFrom</h1>
	 * @return <ul>
	 * 			<li>true if the coordinate is near from the other
	 * 			<li>false if the coordinate isn't near from the other
	 * 		   </ul>
	 * @param coordonn�es a coordinate object to compare
	 */
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

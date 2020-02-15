package com.martin.view;

import java.sql.SQLException;

import com.martin.Connect_SQLite;
import com.martin.Main;
import com.martin.Stats;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.Appareil_Acheteur;
import com.martin.model.appareils.Direction;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.appareils.TypeAppareil;
import com.martin.model.exceptions.NegativeArgentException;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JeuContr�le {
	
	//L'instance de main, pour charger les diff�rentes pages
	Main main;
	
	//La grille principale de jeu
	@FXML GridPane grille;
	@FXML private Label argentLabel;
	@FXML private Label report;
	@FXML private Button upgradeGrid, recherche;
	
	//Le stage de recherche
	Stage research = new Stage();
	
	//---Variables ci-dessous � revoir, modifiers et d�claration.
	private Appareil[][] appareil = new Appareil[20][20];
	private SimpleLongProperty argent;
	private static StringProperty reportProperty = new SimpleStringProperty("");
	private Thread t;
	
	private int tailleGrille = 5;
	
	/*Quelques erreurs l�-dedans, ne faire que quand les appareils seront complets 
	 (tous les classes filles comprises)
	 */
	public void initialize() {
		for(int i = 0; i < TypeAppareil.values().length; i++) {
			try {
				TypeAppareil.values()[i].getClasse().getMethod("initializeData").invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			argent = new SimpleLongProperty(		//Set l'argent sur la somme pr�c�demment enregistr�e
					Connect_SQLite.getInstance()
						.createStatement()
						.executeQuery("SELECT argent FROM infos").getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int x = 0; x < tailleGrille; x++) {
			grille.getColumnConstraints().add(new ColumnConstraints(96));
			grille.getRowConstraints().add(new RowConstraints(96));
			for(int y = 0; y < tailleGrille; y++) {
				try {
					
					String general = getTexte(x, y);
					String niveau = "", type = "", rotation = "";
					int tempoIndex = 0;
					for(int i = 0; i < general.length(); i++) {
						
						if(general.substring(i, i+1).equals("*")) {
							
							type = general.substring(0, i);
							tempoIndex = i;
						}
						
						else if(general.substring(i, i+1).equals("|")) {
							
							niveau = general.substring(tempoIndex+1, i);
							tempoIndex = i;
						}
					}
					rotation = general.substring(tempoIndex+1);
					
					TypeAppareil type2 = TypeAppareil.valueOf(type);
					Direction direction2 = Direction.valueOf(rotation);
					NiveauAppareil niveau2 = NiveauAppareil.valueOf(niveau);
					
					this.appareil[x][y] = type2.getClasse().getConstructor(Coordonn�es.class,
							Direction.class, NiveauAppareil.class, JeuContr�le.class)
							.newInstance(new Coordonn�es(x, y), direction2, niveau2, this);
					this.grille.add(this.appareil[x][y], x, y);
				} catch (Exception e) {
					e.printStackTrace();
					setReport(e.getMessage(), Color.DARKRED);
				}
			}
		}
		grille.setFocusTraversable(true);
		grille.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.M)
					try {
						setArgent(1_000_000_000, true);
					} catch (NegativeArgentException e) {
						System.out.println(e.getLocalizedMessage());
						
					}
				
			}
			
		});
		argentLabel.setText(String.valueOf(argent.get())+" �");
		
		t = new Thread(new Play());
		t.start();
		
		report.textProperty().bind(reportProperty);
		report.setVisible(false);
		report.setTooltip(new Tooltip("Cliquez pour fermer..."));
		
		report.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				report.setVisible(false);
			}
			
		});
		
	}
	//Tirait la case de texte demand�e.
	private String getTexte(int x, int y) throws SQLException {
		return Connect_SQLite.getInstance().createStatement().executeQuery("SELECT * FROM appareils WHERE id = "+(y+1)+";").getString(""+(x+1)+"");
	}
	//A d�placer : sert � d�finir sur quelle instance de main on se base pour changer les fen�tres.
	public void setMainApp(Main main) {
		this.main = main;
	}
	/*La d�finition du Thread � refaire : 
	 * J'ai pens� faire un syst�me de liste de chose � faire via une liste publique et statique
	 * qui va demander l'action. Comme �a, il n'y a que les actions n�cessaires qui sont r�alis�es.
	 * R�sultat: les sols ne font pas lagger pour rien le jeu.*/
	class Play implements Runnable{

		@Override
		public void run() {
			try {
				while(true){
					Thread.sleep(750);
					for(int i = 0; i < Appareil_Acheteur.liste.size(); i++){
						try {
							getGrilleAppareils(Appareil_Acheteur.liste.get(i)).action(Ressource.NONE);
							argentLabel.setTextFill(Color.WHITE);
						} catch (NegativeArgentException e) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									argentLabel.setTextFill(Color.DARKRED);
								}
							});
							
						}
					}
				}
			} catch (IllegalArgumentException | InterruptedException e) {
				System.out.println(
						"ERREUR dans JeuContr�le.Play dans la m�thode run. Raison :\n" + e.getLocalizedMessage());
			}
		}
		
	}
	//On a demand� la fen�tre de recherche
	@FXML private void research() {
		
	}
	//Visuel du bouton AM�LIORER LA GRILLE
	@FXML private void upgradeGrid() {
		/*Si on a pas assez d'argent, plut�t que d'ajouter un message dans le report,
		on ouvre une bo�te de dialogue.*/
	}
	@FXML private void upgradeGridEntered() {
		upgradeGrid.setText("-"+Stats.prixAgrandissement+" �");
	}
	@FXML private void upgradeGridExited() {
		upgradeGrid.setText("AGRANDIR LA GRILLE");
	}
	/**
	 * <h1>setReport</h1>
	 * <p>Add a dialog in the bottom right corner of the grid</p>
	 * 
	 * @param text the text to display
	 * @param border the color of the border of the dialog
	 */
	public  void setReport(String text, Color border) {
		reportProperty.set(text);
		final String hex = String.format("#%02X%02X%02X", ((int)(border.getRed()*255)), 
				(int)(border.getGreen()), (int) (border.getBlue()));
		report.setStyle("-fx-border-color: "+hex+";");
		report.getStyleClass().add("report");
		
		
		report.setVisible(true);
	}
	
	/**
	 * @return the widget grid
	 * 
	*/
	public GridPane getGridPane() {
		return grille;
	}
	/**
	 * @param xy les coordonn�es
	 * @return un Appareil au coordonn�es donn�es
	 */
	public Appareil getGrilleAppareils(Coordonn�es xy){
		return appareil[xy.getX()][xy.getY()];
	}
	/**
	 * @return the argent property
	 */
	public SimpleLongProperty getArgentProperty() {
		return argent;
	}
	/**
	 * @return la taille de la grille
	 */
	public int getTailleGrille() {
		return tailleGrille;
	}
	
	/**
	 * @author Martin
	 * 28 janv. 2020 | 15:14:44
	 * 
	 * <b>setAppareil</b>
	 * <p>Modifie une case d'appareil.</p>
	 * 
	 * Args :
	 * @param xy coordonn�es du nouvel appareil
	 * @param appareil l'appareil qui remplace
	 * 
	*/
	public void setAppareil(Coordonn�es xy, Appareil appareil) {
		try {
			setArgent(((int) appareil.getType().getClasse().getMethod("getPrix").invoke(null))
					, false);
			this.appareil[xy.getX()][xy.getY()] = appareil;
			grille.add(this.appareil[xy.getX()][xy.getY()], xy.getX(), xy.getY());
		} catch (NegativeArgentException e) {
			setReport(e.getMessage(), Color.DARKRED);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @author Martin
	 * 
	 *         <b>setArgent</b>
	 *         <p>
	 *         Modifie le solde d'argent en jeu
	 *         </p>
	 * 
	 * @param somme    la somme d'argent � enlever ou supprimer
	 * @param increase vaut true pour ajouter, vaut false pour enlever de l'argent
	 * 
	 * @throws NegativeArgentException Si la somme d'argent devient n�gative
	 * 
	 */
	public void setArgent(long somme, boolean increase) throws NegativeArgentException{
		if(increase)
			argent.set(argent.get()+somme);
		else {
			if(argent.get() >= somme)
				argent.set(argent.get()-somme);
			else {
				throw new NegativeArgentException();
			}
		}
		
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				argentLabel.setText(String.valueOf(argent.get())+" �");
			}
		});
	}
}
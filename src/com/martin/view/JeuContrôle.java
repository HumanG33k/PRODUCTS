package com.martin.view;

import java.util.List;

import com.martin.Connect_SQLite;
import com.martin.Main;
import com.martin.Partie;
import com.martin.Stats;
import com.martin.model.Coordonn�es;
import com.martin.model.Ressource;
import com.martin.model.appareils.Appareil;
import com.martin.model.appareils.Appareil_Acheteur;
import com.martin.model.appareils.Appareil_Sol;
import com.martin.model.appareils.Direction;
import com.martin.model.appareils.NiveauAppareil;
import com.martin.model.exceptions.NegativeArgentException;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JeuContr�le {
	
	//L'instance de main, pour charger les diff�rentes pages
	public static Main main;
	
	@FXML GridPane grille;
	@FXML private Label argentLabel;
	@FXML private Label report;
	@FXML private Button upgradeGrid, recherche;
	
	//Le stage de recherche
	Stage research = new Stage();
	
	//---Variables ci-dessous � revoir, modifiers et d�claration.
	private Appareil[][] appareil = new Appareil[20][20];
	private StringProperty reportProperty = new SimpleStringProperty();
	private Thread t;
	
	private static Partie partieEnCours;
	
	
	public void initialize() {}
	public void setMainApp(Main main, Partie partieToLoad) throws Exception {
		JeuContr�le.main = main;
		JeuContr�le.partieEnCours = partieToLoad;
		
		argentLabel.setText(String.valueOf(partieToLoad.getArgent()));
		
		List<Appareil> listeAppareils = 
				Connect_SQLite.getAppareilDao().queryForEq("partie_idPartie", partieToLoad.getID());
		
		for(Appareil appareil : listeAppareils) {
			setAppareil(appareil.getXy(), Appareil.getInstance(appareil), true);
		}
		for(int x = 0; x < partieToLoad.getTailleGrille(); x++) {
			for(int y = 0; y < partieToLoad.getTailleGrille(); y++) {
				if(this.appareil[x][y] == null) {
					setAppareil(new Coordonn�es(x, y), new Appareil_Sol(new Coordonn�es(x, y), Direction.UP, 
							NiveauAppareil.NIVEAU_1, this, partieToLoad), true);
				}
			}
		}
		
		grille.setFocusTraversable(true);
		argentLabel.setText(String.valueOf(partieToLoad.getArgent())+" �");
		
		t = new Thread(new Play());
		t.start();
		
		report.textProperty().bind(reportProperty);
		report.setVisible(false);
		report.setTooltip(new Tooltip("Cliquez pour fermer..."));
		
		partieToLoad.save();
		
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
							Ressource[] res = {Ressource.NONE};
							getGrilleAppareils(Appareil_Acheteur.liste.get(i)).action(res);
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
	public long getArgent() {
		return partieEnCours.getArgent();
	}
	/**
	 * @return la taille de la grille
	 */
	public int getTailleGrille() {
		return partieEnCours.getTailleGrille();
	}
	public static JeuContr�le get() {
		try {
			JeuContr�le controller = new JeuContr�le();
			controller.initialize();
			controller.setMainApp(main, partieEnCours);
			return controller;
		} catch (Exception e) {
			return null;
		}
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
	public void setAppareil(Coordonn�es xy, Appareil appareil, boolean ignoreCost) {
		try {
			if(!ignoreCost)
				setArgent(((int) appareil.getType().getClasse().getMethod("getPrix").invoke(null))
						, false);
			this.appareil[xy.getX()][xy.getY()] = appareil;
			grille.add(this.appareil[xy.getX()][xy.getY()], xy.getX(), xy.getY());
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
	 * @param somme la somme d'argent � enlever ou supprimer
	 * @param increase vaut true pour ajouter, vaut false pour enlever de l'argent
	 * 
	 * @throws NegativeArgentException Si la somme d'argent devient n�gative
	 * 
	 */
	public void setArgent(long somme, boolean increase){
		//Modifie le solde d'argent de la partie en cours
		final boolean edit = partieEnCours.setArgent(somme, increase);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				if(edit){
					//Modifie le lib�ll� d'argent
					argentLabel.setText(String.valueOf(partieEnCours.getArgent()+" �"));
				}
			}
		});
	}
}
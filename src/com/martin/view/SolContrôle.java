package com.martin.view;

import com.martin.model.appareils.TypeAppareil;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/*
 * Martin
 * 21/01/2020
 * 
 * Contr�leur de Sol.fxml qui sert pour la construction des appareils.*/

public class SolContr�le {
	
	@FXML private Label coordonn�es; //Label des coordonn�es situ� en haut de la page
	@FXML private VBox listeAppareils;	//Liste des appareils int�gr� dans le ScrollPane au milieu � droite
	
	private Dialog<Object> dialog;
	
	/*
	 * Martin
	 * 21/01/2020
	 * 
	 * R�le : Mettre en forme la fen�tre, ajouter les layouts et les widgets.*/
	public void initialize() {
		for(int i = 0; i < TypeAppareil.values().length-1; i++) {
			//On ajoute les displayers des appareils dans la liste pr�vue � cet effet
			listeAppareils.getChildren().add(new Displayer(TypeAppareil.values()[i]));
			
			//On ajoute un listener du clic sur un displayer
			listeAppareils.getChildren().get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
				
				@Override
				public void handle(MouseEvent event) {
					try {
						dialog.setResult(((Displayer) event.getSource()).getTypeAppareil());
						dialog.close();
					} catch (Exception e) {
						System.out.println("ERREUR lors de la construction d'un appareil. Raison : "+e.getMessage());
					}
				}
				
			});
		}
		
	}
	
	/*
	 * Martin
	 * 21/01/2020
	 * 
	 * R�le : met � jour les labels en fonction de donn�es pass�es en param�tres.
	 * Args : x -> coordonn�es x de l'appareil
	 * 		  y -> coordonn�es y de l'appareil
	 *        dialog -> la bo�te de dialogue � fermer*/
	public void setMainApp(int x, int y, Dialog<Object> dialog) {
		coordonn�es.setText("X: "+x+" Y: "+y);
		this.dialog = dialog;
	}
}

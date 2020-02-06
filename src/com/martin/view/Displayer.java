package com.martin.view;

import java.io.File;
import java.io.FileInputStream;

import com.martin.model.Ressource;
import com.martin.model.appareils.TypeAppareil;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/*
 * Martin
 * 20/01/2020
 * 
 * R�le : �cran � ressource ou � appareil, principalement utilis� dans SolContr�le.java et AppareilsContr�le.java*/
public class Displayer extends BorderPane {
	
	//Labels nom, infos et image pour avoir un aper�u de la ressource ou de l'appareil
	private Label nom;
	private ImageView image;
	private Label infos;
	
	//Stock pour avoir obtenir l'�l�ment � l'origine de l'�cran
	private Ressource ressource;
	private TypeAppareil typeAppareil;
	
	/*
	 * Martin
	 * 20/01/2020
	 * 
	 * --- CONSTRUCTEUR ---
	 * 
	 * R�le : Construit un �cran � partir d'une ressource*/
	public Displayer(Ressource ressource) {
		
		try {
			this.ressource = ressource;
			
			nom = new Label();
			nom.setAlignment(Pos.TOP_CENTER);
			nom.setText(ressource.getNom());
			this.setTop(nom);
			
			
			image = new ImageView();
			image.setImage(new Image(new FileInputStream(new File(ressource.getURL()))));
			this.setCenter(image);
			
			
			infos = new Label();
			infos.setAlignment(Pos.TOP_CENTER);
			infos.setText("Prix de vente : "+String.valueOf(ressource.getValue()) +" �");
			this.setBottom(infos);
			
			
		} catch(Exception e) {
			System.out.println("ERREUR lors de la cr�ation d'un Displayer � partir d'une Ressource. "
					+ "Raison :\n"+e.getMessage());
		}
	}
	
	/*
	 * Martin
	 * 21/01/2020
	 * 
	 * --- CONSTRUCUTEUR ---
	 * 
	 * R�le : Construit un �cran � partir d'un type d'appareil*/
	public Displayer(TypeAppareil appareil){
		try {
			this.typeAppareil = appareil;
			
			nom = new Label();
			nom.setUnderline(true);
			nom.setAlignment(Pos.TOP_CENTER);
			nom.setText(appareil.getNom());
			nom.setWrapText(true);
			this.setTop(nom);
			
			
			image = new ImageView();
			image.setImage(new Image(new FileInputStream(new File("images/machines niveau 1/"+appareil.getURL()))));
			this.setRight(image);
			
			
			infos = new Label();
			infos.setAlignment(Pos.TOP_CENTER);
			infos.setText("\nPrix : "+String.valueOf( appareil.getClasse()
					.getMethod("getPrix").invoke(null))+" �\n\n"+appareil.getDescription());
			infos.setWrapText(true);
			this.setLeft(infos);
			
			this.setPadding(new Insets(3, 3, 3, 3));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				final String style = "-fx-background-color: #3b4044;";
				
				((Displayer) e.getSource()).setStyle(style);
				nom.setStyle(style);
				infos.setStyle(style);
				
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				final String style = "-fx-background-color: #242729;";
				
				((Displayer) e.getSource()).setStyle(style);
				nom.setStyle(style);
				infos.setStyle(style);
				
			}
		});
	}
	
	
	/*
	 * Martin
	 * 20/01/2020
	 * 
	 * --- GETTERS ---
	 * 
	 * R�les : Permettent d'obtenir les widgets utilis�s.*/
	public Label getNom() {
		return nom;
	}
	public ImageView getImage() {
		return image;
	}
	public Label getInfos() {
		return infos;
	}
	
	/*
	 * Martin 
	 * 21/01/2020
	 * 
	 * --- GETTERS ---
	 * 
	 * R�les : Permettent d'obtenir l'�l�ment � l'origine de l'appareil.
	 * 	Peut retourner null si l'origine demand�e ne correspond pas � celle utilis�e
	 * 	  (Ex : construit avec un appareil, et demande une ressource)*/
	public Ressource getRessource() {
		return ressource;
	}
	public TypeAppareil getTypeAppareil() {
		return typeAppareil;
	}
}

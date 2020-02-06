package com.martin.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import com.martin.model.appareils.TypeAppareil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class AppareilsContr�le {

	@FXML
	Label coordonn�es;
	@FXML
	VBox listeAppareils;

	public AppareilsContr�le() {
	}

	public void initialize() {
		for (int i = 0; i < TypeAppareil.values().length - 1; i++) {
			listeAppareils.getChildren().add(new Displayer(TypeAppareil.values()[i]));
		}

		//CORRIGER l'ouverture de la fen�tre d'un appareil
		/*
		 * //Set de l'image du destroy et upgrade parce que en jar ca marchait pas via
		 * fxml //Report d'une ouverture de fen�tre
		 * 
		 * //Si c'est un sol remove la tab actions //Sinon remove la tab appareils et
		 * set en disbale le bouton upgrade en fonction de la recherche du //niveau actu
		 * de l'appareil
		 * 
		 * 
		 * //Boucles for pour la grille des descriptions � revoir en fonction de la
		 * taille de TypeAppareil for(int i = 0; i<5; i++) { for(int j = 0; j<2; j++) {
		 * try { //Variables locales name, desc et img de des appareils //A revoir parce
		 * que c'est pas DU TOUT optimis�
		 * 
		 * //Switch en fonction de la longueur faire un objet Affiche ou du genre qui
		 * display //la desc d'un appareil en fonction d'un type appareil switch(i) {
		 * case 0: if(j == 0) { //Ici c'set les acheteurs d�finitions name desc img
		 * 
		 * if(estLeM�meAppareil(url)) { styliser(bpt[j][i], url);
		 * 
		 * //Si c'�tait le m�me appareil bah on cr�eait encore des tonnes de variables
		 * 
		 * //Les sets qui vont avec
		 * 
		 * //La c'est sp�cial c'est un acheteur //On clique dessus ca changeait la
		 * ressource
		 * 
		 * //On oublie pas de positionner comme il faut } }
		 * 
		 * //La c'est plein d'autres appareils basiques qui n'ont pas de truc
		 * particulier //Convoyeurs, Vendeur , Four, Presse, Fils avec les def de name,
		 * desc, et img case 4: if(j == 0) { //Le trieur : les defs de base
		 * 
		 * //clique crit�re 2 avec un CraftingContr�le du bled (les trucs statics qui
		 * //n'ont rein � faire l�)
		 * 
		 * //idem crit 1
		 * 
		 * //Placement de la tab sp�cial trieur } else { //defs de base //[seulement si
		 * l'appareil correspond] def clique sortie pour choisir le sch�ma } break; }
		 * 
		 * //Set quand on clique sur la case //couleur et report + set image
		 * 
		 * //Set du positionnement de name, desc, img
		 * 
		 * //Set quand on entre sur la case d'un appareil avec //UN GROS SWITCH DE MORT
		 * 
		 * //Set quand on sort de la case retour au blanc ou gris fonc�
		 * 
		 * //Positionnement
		 * 
		 * }catch(FileNotFoundException e) {} } }
		 */

	}

	// Le setMainApp perdu au milieu de la jungle avec du STATIQUE en plus avec des
	// param�tres qui servent � rien

	public void setMainApp(int i, int j) {
		coordonn�es.setText("Hello les appareils !!");

	}

	@FXML private void sauvegarder() { //J'appelle cela du gros bricolage...
	  if(appareils.isSelected()) { if(cost.get() <= JeuContr�le.argent.get()) {
	  //Sans oublier de fermer la fen�tre try {
	  
	  //Des defs statiques qui m'horripile(rota, url...)
	  
	  //Conversion de la rotation
	  
	  //Retrait de l'argent
	  
	  //GROS SWITCH DE MORT
	  
	  //Set database la case
	  
	  } catch (FileNotFoundException | SQLException e) { e.printStackTrace(); } }
	  else { //Un va te faire foutre tu n'a pas assez d'argent dans la console } }
	  else if(actions.isSelected()) { //Fermer la fen�tre if(upgradeBoolean == 1) {
	  //Clause if avec int soit-disant boolean //Ancienne commande avec
	  LocatedImageView
	  
	  //Conversion rota (comme une sensation de d�j� vu
	  
	  //Set database la case } else if(upgradeBoolean == 2){ //Quand destroy sets
	  case, database
	  
	  } } }**

	@FXML private void annuler() { // Rien qu'une m�thode juste pour quitter }

	@FXML private void UpgradePressed() { //Quand on clique sur upgrade sets
	  argent si assez et appel sauvagarder plus haut }**

	@FXML private void upgradeEntered() { //Quand on entre sur la case upgrade
	  GROS SWITCH DE MORT avec le set de cost }**

	@FXML private void upgradeExited() { // Reset on quitte la case }

	@FXML private void DestroyPressed() { //Quand on clique sur la case destroy
	  GROS SWITCH DE MORT et le set de cost
	  
	  sauvegarder(); }**

	@FXML private void DestroyEntered() { //Quand on arrive sur la case avec GROS
	  SWITCH DE MORT }**

	@FXML private void DestroyExited() { //Reset quand on quitte la case }
	  
	  //Des setters pour les labels et images d'entr�es sorties par un moyen
	  carr�ment pas L�GAL

	public void setLabelEntr�e(String str) {

	}

	public void setLabelSortie(String str) {
		labelSortie.setText(str);
	}

	public static void setImgCrit1(String url) {
		try {
			img1.setImage(new Image(new FileInputStream(new File(url))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void setImgCrit2(String url) {
		try {
			img2.setImage(new Image(new FileInputStream(new File(url))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setImgEntr�e(String str) throws FileNotFoundException {
		imgEntr�e.setImage(new Image(new FileInputStream(new File(str))));
	}

	public void setImgSortie(String str) throws FileNotFoundException {
		imgSortie.setImage(new Image(new FileInputStream(new File(str))));
	}

}
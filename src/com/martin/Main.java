package com.martin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.martin.view.Accueil2Contr�le;
import com.martin.view.AccueilContr�le;
import com.martin.view.JeuContr�le;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/*
 * Martin
 * --/--/----
 * 
 * R�le : Lance l'application*/
public class Main extends Application {
	
	
	public static Stage stage;  //La fen�tre
	
	//La m�thode main
	public static void main(String[] args) {
		/*
		 * La ligne ci-dessous �x�cute quelques param�tres syst�mes puis 
		 * lance la m�thode start juste en-dessous.
		 * Elle se charge de faire le setup du programme pour tout poser sur de 
		 * bonnes bases.
		*/
		
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		/*
		 * Le fichier Stats contient des donn�es du jeu qui doivent �tre accesible 
		 * partout.
		 * La m�thode initialize charge les donn�es de la base de donn�es pour un acc�s plus rapide
		 * et plus clair plus tard dans le programme.*/
		Stats.initialize();
		
		try {
			stage = primaryStage;
			stage.setTitle("PRODUCTS.");
			stage.setResizable(false);
			stage.getIcons().add(new Image(new FileInputStream(new File("images/Icone.png"))));
			//On charge le pseudo
			ResultSet res = Connect_SQLite.getInstance().createStatement().executeQuery(
					"SELECT pseudo FROM infos;");
			//Si le pseudo est vide, on demande le pseudo, sinon on passe � l'�tape suivante.
			while(res.next()) {
				if(res.getString(1) != null) {
					initAccueil2();
				}
				else {
					initAccueil();
				}
			}
		} catch (SQLException | FileNotFoundException e) {
			e.printStackTrace();
		}
		stage.show(); //On ouvre la fen�tre/le stage.
	}
	
	
	public void initAccueil() {
		FXMLLoader loader = new FXMLLoader();	//Permet de charger des fichier .fxml
		loader.setLocation(Main.class.getResource("view/Accueil.fxml"));	//D�finit l'emplacement o� chercher
		
		try {
			BorderPane conteneurPrincipal = (BorderPane) loader.load();		//Charge le fichier dans notre variable de contenu, comme pr�vu
			Scene scene = new Scene(conteneurPrincipal);	//Fen�tre dans laquelle s'affiche notre contenu
			stage.setScene(scene);	//Montre � la fen�tre quel stage utiliser, et donc son contenu
			
			
			
			AccueilContr�le controler = loader.getController();	//Le contr�leur du fichier en question
			controler.setMainApp(this);	//Petite indication au contr�leur
			
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {	//�venements claviers...

				@Override
				public void handle(KeyEvent e) {
					if(e.getCode() == KeyCode.ENTER) {	//Si on tape sur entr�e
						controler.seConnecter();
					}
				}
				
			});
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void initAccueil2() {
		FXMLLoader loader = new FXMLLoader();	//Permet de charger des fichier .fxml
		loader.setLocation(Main.class.getResource("view/Accueil2.fxml"));	//D�finit l'emplacement o� chercher
		
		try {
			
			
			BorderPane conteneurPrincipal = (BorderPane) loader.load();		//Charge le fichier dans notre variable de contenu, comme pr�vu
			Scene scene = new Scene(conteneurPrincipal);	//Fen�tre dans laquelle s'affiche notre contenu
			stage.setScene(scene);	//Montre � la fen�tre quel stage utiliser, et donc son contenu
			
			Accueil2Contr�le controler = loader.getController();
			controler.setMainApp(this);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void initGame() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Jeu.fxml"));
			
			BorderPane Bp = (BorderPane) loader.load();
			Scene scene = new Scene(Bp);
			stage.setScene(scene);
			
			stage.setResizable(true);
			
			JeuContr�le controler = loader.getController();
			controler.setMainApp(this);
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					try {
							Stats.sauvegarder();
							System.exit(0);
					}catch(SQLException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText("Petit probl�me... Nous n'avons pas pu sauvegarder vos donn�es !");
						alert.setContentText("Vous avez peut-�tre supprimer le fichier de donn�es ! \n"
								+ "Je vous conseille de r�installer le jeu, m�me si vos donn�es seront perdu � jamais !!");
						alert.initOwner(stage);
						alert.show();
					}
				}
			});
		} catch (IOException e) {
			System.out.println("ERREUR dans Main dans la m�thode " + "initGame. Raison :\n" + e.getLocalizedMessage());
		}
	}
	public Stage getStage() {
		return stage;
	}

}
package com.martin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.martin.view.Accueil2Contr�le;
import com.martin.view.AccueilContr�le;
import com.martin.view.JeuContr�le;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	
	public static Stage stage;  //La fen�tre
	
	public static void main(String[] args) {
		launch(args);
	
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//D�finition principale du stage
			stage = primaryStage;
			stage.setTitle("PRODUCTS.");
			stage.setResizable(false);
			stage.getIcons().add(new Image(new FileInputStream(new File("images/Icone.png"))));
			
			//Tests pour savoir si des parties sont disponibles
			if(Connect_SQLite.getPartieDao().queryForAll().size() == 0) {
				//Si il n'y en a pas, alors on dirige l'utilisateur de cr�ation de partie
				initAccueil();
			}
			else {
				//Sinon, on le dirige vers la s�lection de partie
				initAccueil2();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Petit probl�me... L'ic�ne n'a pas pu �tre charg� correctement.");
		} catch (SQLException e) {
			System.err.println("Petit probl�me... La connexion � la base de donn�es a �chou�.");
			e.printStackTrace();
			
		}
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				System.exit(0);
			}
		});
		stage.show(); //Et on oublit pas d'afficher la fen�tre
	}
	
	/**
	 * <h1>initAccueil</h1>
	 * <p>Initialize the stage with the view Accueil.fxml, who corresponds to a the first start page 
	 * (when no login is registered).</p>
	 * 
	 */
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
	/**
	 * <h1>initAccueil2</h1>
	 * <p>Initialize the stage with the view Accueil2.fxml, who demands to load the registered game or to begin a 
	 * new game. (when a login is regestered)</p>
	 */
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
	/**
	 * <h1>initGame</h1>
	 * <p>Initialize the stage with the view Jeu.fxml, who loads all the images and resources to do this game
	 * functionnal.</p>
	 * @throws Exception 
	 */
	public void initGame(Partie partie) throws Exception {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Jeu.fxml"));
			
			BorderPane Bp = (BorderPane) loader.load();
			Scene scene = new Scene(Bp);
			stage.setScene(scene);
			
			stage.setResizable(true);
			
			JeuContr�le controler = loader.getController();
			controler.setMainApp(this, partie);
		} catch (IOException e) {
			System.err.println("ERREUR dans Main dans la m�thode " + "initGame. Raison :\n" + e.getLocalizedMessage());
		}
	}

}
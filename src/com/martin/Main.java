package com.martin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.martin.model.Coordonnees;
import com.martin.model.Paquet;
import com.martin.model.Ressource;
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

	public static Stage stage;

	public static void main(String[] args) {
		Coordonnees.insert(new Coordonnees(0, 0));
		Coordonnees.insert(new Coordonnees(1, 0));

		Paquet.insert(new Paquet(Ressource.ALUMINIUM, 2));

		Partie.insert(new Partie(
				"Ce nom de partie est un peu long, non. Je suis pas s�r, je devrai r�fl�chir un peu plus la prochaine fois, peut-�tre �ventuellement."));

		Coordonnees.query();
		Paquet.query();
		Partie.query();

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) {
		// The session for later
		Session session = Connect_SQLite.getSession();

		try {
			// Little stage paramatering
			stage = primaryStage;
			stage.setTitle("PRODUCTS.");
			stage.setResizable(false);
			stage.getIcons().add(new Image(
					new FileInputStream(new File("images/Icone.png"))));

			// Little query to select which menu will be displayed at first
			Query<Partie> query = session.createQuery(
					"from Partie",
					Partie.class);
			List<Partie> list = query.list();

			// Then one or the other
			if (list.size() == 0)
				initAccueil();
			else {
				initAccueil2();
			}
		} catch (FileNotFoundException e) {
			System.err.println(
					"Oh ! There is a mistake ! The logo can't be loaded...");
		} catch (HibernateException e) {
			System.err.println(
					"Oh ! There is a mistake ! The games can't be loaded...");
		} finally {
			if (session != null)
				session.close();
		}
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				System.exit(0);
			}
		});
		// Show the main stage
		stage.show();
	}

	/**
	 * Initialize the stage with the view Accueil.fxml, who corresponds to
	 * a the first start page (generally when no game can be found on the
	 * database).
	 * 
	 * @see AccueilContr�le
	 * @see AccueilContr�le#setMainApp(Main)
	 */
	public void initAccueil() {
		// Permet de cherger des fichiers .fxml
		FXMLLoader loader = new FXMLLoader();
		// D�finit l'emplacement o� chercher
		loader.setLocation(Main.class.getResource("view/Accueil.fxml"));

		try {
			// Charge le fichier dans notre variable de contenu, comme pr�vu
			BorderPane conteneurPrincipal = (BorderPane) loader.load();
			// Fen�tre edans laquelle s'affiche notre contenu
			Scene scene = new Scene(conteneurPrincipal);
			// Montre � la fen�tre quel sc�ne utiliser
			stage.setScene(scene);

			// Le contr�leur de la fen�tre
			AccueilContr�le controler = loader.getController();
			controler.setMainApp(this);

			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent e) {
					if (e.getCode() == KeyCode.ENTER) { // Si on tape sur entr�e
						controler.seConnecter();
					}
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Initialize the stage with the view Accueil2.fxml, who demands to
	 * load the registered game or to begin a new game. (when at least one
	 * game is registered)
	 * 
	 * @see Accueil2Contr�le
	 * @see Accueil2Contr�le#setMainApp(Main)
	 */
	public void initAccueil2() {
		// Permet de charger des fichiers .fxml
		FXMLLoader loader = new FXMLLoader();
		// D�finit l'emplacement o� chercher le fichier
		loader.setLocation(Main.class.getResource("view/Accueil2.fxml"));

		try {
			// Charge le fichier dans notre variable de contenu, comme pr�vu
			BorderPane conteneurPrincipal = (BorderPane) loader.load();
			// Fen�tre dans laquelle s'affiche notre contenu
			Scene scene = new Scene(conteneurPrincipal);
			// Montre � la fen�tre quel stage utiliser, et donc son contenu
			stage.setScene(scene);

			// Le contr�leur de la fen�tre
			Accueil2Contr�le controler = loader.getController();
			controler.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Initialize the stage with the view Jeu.fxml, who loads all the
	 * images and resources to do this game functionnal.
	 * 
	 * @param partie the game to load
	 * 
	 * @see JeuContr�le
	 * @see JeuContr�le#load(Partie)
	 * @see JeuContr�le#setMainApp(Main)
	 */
	public void initGame(Partie partie) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Jeu.fxml"));

			BorderPane Bp = (BorderPane) loader.load();
			Scene scene = new Scene(Bp);
			stage.setScene(scene);

			stage.setResizable(true);

			JeuContr�le controller = loader.getController();
			controller.setMainApp(this);
			controller.load(partie);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					try {
						controller.returnToHome();
					} catch (SQLException e1) {
						System.out.println(e1.getLocalizedMessage());

					}
					System.exit(0);
				}
			});
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
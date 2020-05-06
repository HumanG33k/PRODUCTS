package com.martinheywang.view;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.martinheywang.Database;
import com.martinheywang.Main;
import com.martinheywang.model.Coordinates;
import com.martinheywang.model.Game;
import com.martinheywang.model.Packing;
import com.martinheywang.model.devices.Buyer;
import com.martinheywang.model.devices.Device;
import com.martinheywang.model.devices.DeviceModel;
import com.martinheywang.model.exceptions.MoneyException;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class GameController {

	private Main main;

	@FXML
	private GridPane grille;
	@FXML
	private Label argentLabel;
	@FXML
	private Label report;
	@FXML
	private Button upgradeGridButton;
	@FXML
	private ProgressBar progression;

	private static final StringProperty reportProperty = new SimpleStringProperty();
	private static final LongProperty argentProperty = new SimpleLongProperty();

	private Thread t;
	private Game partieEnCours;

	public void initialize() {
		report.textProperty().bind(reportProperty);
		report.setVisible(false);
		report.setTooltip(new Tooltip("Cliquez pour cacher..."));
		report.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				report.setVisible(false);
			}
		});
		argentProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
				argentLabel.setText(nf.format(newValue) + " €");

			};
		});
		grille.setFocusTraversable(true);
	}

	/**
	 * This method sets which instance of main should be used.
	 * 
	 * @param main the instance of main
	 */
	public void setMainApp(Main main) {
		this.main = main;
	}

	/**
	 * Loads a game with all its informations and lauch the thread.
	 * 
	 * @param partieToLoad the game to load
	 */
	public void load(Game partieToLoad) throws SQLException {
		// Save this instance (used a little bit later)
		GameController controller = this;
		// The task defines how to load the game
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					// Little message ("The game is still loading...")
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							setReport(
									"Chargement de la partie en cours...\n"
											+ "L'opération peut durer quelques instants.",
									Color.INDIANRED);
						}
					});

					// Updating the field partieEnCours
					partieEnCours = partieToLoad;
					// Fetching the model of all devices in a list
					List<DeviceModel> devicesModel = partieToLoad
							.getAppareilsModel();

					final int taille = partieToLoad.getTailleGrille();
					// Creating the device if they aren't enough
					if (devicesModel.size() < Math.pow(taille, 2)) {
						for (int x = 0; x < taille; x++) {
							for (int y = 0; y < taille; y++) {
								try {
									final DeviceModel model = new DeviceModel(
											new Coordinates(x, y),
											partieToLoad);
									devicesModel.add(model);
									Database.daoDeviceModel().create(model);
								} catch (SQLException e) {
									// Catch SQLException, because we are
									// interacting with the database
									e.printStackTrace();
								}
							}
						}
					}
					// Reseting the list of buyer to fix a bug
					Buyer.liste = new ArrayList<Coordinates>();

					// Variable i for progress
					int i = 1;
					// For all models
					for (DeviceModel model : devicesModel) {
						// Creating a new device using the model
						Device device = model.getType().getClasse()
								.getConstructor(DeviceModel.class,
										GameController.class)
								.newInstance(model, controller);

						// Adding it to the grid (view)
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								grille.add(device,
										device.getModel().getCoordinates()
												.getX(),
										device.getModel().getCoordinates()
												.getY());
							}
						});

						// Setting up the progress
						i++;
						progression.progressProperty()
								.set((double) i
										/ devicesModel.size());
					}

					// Starting the thread of game
					t = new Thread(new Play());
					t.start();

					// Last view modifications
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							argentLabel.setVisible(true);
							progression.setVisible(false);
							// Sets the report dialog to a little text who says
							// that
							// the game is
							// still loading and sets the money label
							setReport("Bienvenue !", Color.CORNFLOWERBLUE);
							argentProperty.set(partieToLoad.getArgent());
						}
					});
				} catch (Exception e) {
					System.err.println(
							"An error occured when loading the game. Here is the full error message :\n\n\n");
					e.printStackTrace();
				}
				// Here we must return something of type Void (this type can't
				// be instantiated), so we return null
				return null;
			}
		};

		// Launching obviously the task defined in a new thread
		Thread loading = new Thread(task);
		loading.start();

	}

	/**
	 * This method closes the game and set up the stage on the home scene,
	 * where we can chose which game we want to load.
	 * 
	 * @see Main#initAccueil2()
	 */
	@FXML
	public void returnToHome() throws SQLException {
		t.interrupt();
		partieEnCours.save();
		main.initAccueil2();
	}

	@FXML
	public void research() {
		// Todo : research frame
	}

	@FXML
	public void upgradeGrid() {
		try {
			// Defining the new size
			final int newTaille = partieEnCours.getTailleGrille() + 1;

			// Get the price of this action
			ResourceBundle bundle = ResourceBundle
					.getBundle("com.martin.model.bundles.GrilleUpdate");
			// This action might throw an exception : if we don't have enough
			// money
			// It the MoneyException is throwed, this method breaks and nothing
			// is done.
			setArgent(Long.valueOf(bundle.getString(String.valueOf(newTaille))),
					false);

			partieEnCours.setTailleGrille(newTaille);
			// From 0 to the new size in each dimension
			for (int x = 0; x < newTaille; x++) {
				for (int y = 0; y < newTaille; y++) {
					// If no devices exists in this zone
					if (findDevice(new Coordinates(x, y)) == null) {
						// Crete a new Model for Device
						final DeviceModel model = new DeviceModel(
								new Coordinates(x, y),
								partieEnCours);
						// Save the new model in the database
						Database.daoDeviceModel().create(model);

						// Create the new device
						Device device = model.getType().getClasse()
								.getConstructor(DeviceModel.class,
										GameController.class)
								.newInstance(model, this);
						// Add it to the grid
						grille.add(device,
								device.getModel().getCoordinates().getX(),
								device.getModel().getCoordinates().getY());
					}
				}
			}

			// Save the game
			partieEnCours.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class Play implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(750);
					for (int i = 0; i < Buyer.liste.size(); i++) {
						try {
							findDevice(Buyer.liste.get(i))
									.action(new Packing());
							argentLabel.setTextFill(Color.WHITE);
						} catch (MoneyException e) {
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
			}
		}

	}

	/**
	 * Iterates over the main GridPane children's list and returns the
	 * first obejct in the list where the coordinates matches the the
	 * given parameter.
	 * 
	 * @param xy a Coordinates' instance
	 * @return a device in the main grid
	 */
	public Device findDevice(Coordinates xy) {
		for (Node node : grille.getChildren()) {
			if (node instanceof Device) {
				final Coordinates nodeXy = ((Device) node).getModel()
						.getCoordinates();
				if (nodeXy.getX() == xy.getX() && nodeXy.getY() == xy.getY()) {
					return (Device) node;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * Sets the amount of money of the current game.
	 * 
	 * @param somme    how many money should be added or substracted
	 * @param increase a boolean (true to increase, false to decrease)
	 */
	public void setArgent(long somme, boolean increase) throws MoneyException {
		// This action has to be performed on the main JavaFX Thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// If the value is true, increases the money
				if (increase) {
					argentProperty.set(argentProperty.get() + somme);
				}
				// Else, decrease
				else {

					if (argentProperty.get() < somme) {
					} else {
						argentProperty.set(argentProperty.get() - somme);
					}
				}
			}
		});
		partieEnCours.setArgent(somme, increase);
	}

	/**
	 * Replaces a device at the given coordinates of the new device.
	 * 
	 * @param device     the new device that replaces the old
	 * @param ignoreCost allow the user to avoid the cost of the operation
	 * @throws MoneyException in case the money is set, if the money
	 *                        amount is too low.
	 */
	public void setAppareil(Device device, boolean ignoreCost)
			throws MoneyException {
		// Setting the device in the database
		partieEnCours.setAppareil(device.getModel());
		// If we don't want to avoid the price
		if (!ignoreCost) {
			// Set the money to the game
			setArgent(device.getModel().getType().getPrix(), false);
		}
		// For all nodes in the main gripane
		for (Node node : grille.getChildren()) {
			// If it is a Device
			if (node instanceof Device) {
				// Getting its coordinates
				final Coordinates nodeXy = ((Device) node).getModel()
						.getCoordinates();
				// if the coordinates matches
				if (nodeXy.getX() == device.getModel().getCoordinates().getX()
						&& nodeXy.getY() == device.getModel().getCoordinates()
								.getY()) {
					// Remove the old device and adding the new
					grille.getChildren().remove(node);
					grille.add(device,
							device.getModel().getCoordinates().getX(),
							device.getModel().getCoordinates().getY());
					break;
				}
			}
		}
	}

	/**
	 * Shows the report if hided and sets the text to String in parameter.
	 * The must also indicates a JavaFX Color which will be the color of
	 * the border.
	 * 
	 * @param text        the text to show
	 * @param colorBorder the new color of the border
	 */
	public void setReport(String text, Color colorBorder) {
		reportProperty.set(text);
		report.getStyleClass().add("report");
		report.setStyle("-fx-border-color: " + String.format("#%02X%02X%02X",
				(int) (colorBorder.getRed() * 255),
				(int) (colorBorder.getGreen() * 255),
				(int) (colorBorder.getBlue() * 255)) + ";");
		report.setVisible(true);
	}

	public Game getPartieEnCours() {
		return partieEnCours;
	}
}
package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.Main;
import com.martin.model.Coordinates;
import com.martin.model.Stock;
import com.martin.model.appareils.orientation.Entrances;
import com.martin.model.appareils.orientation.Exits;
import com.martin.view.JeuContr�le;
import com.martin.view.SolContr�le;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class Floor extends Device {

	public Floor(DeviceModel model, JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("view/Sol.fxml"));

					Dialog<Object> dialog;
					DialogPane dialogPane;

					dialogPane = (DialogPane) loader.load();
					dialog = new Dialog<Object>();
					dialog.setTitle("S�lectionnez un appareil � construire");
					dialog.setDialogPane(dialogPane);
					dialog.initOwner(Main.stage);
					dialog.initModality(Modality.NONE);

					SolContr�le SController = loader.getController();
					SController.setMainApp(
							new Coordinates(model.getCoordonnees().getX(),
									model.getCoordonnees().getY()),
							dialog);

					dialog.showAndWait();

					if (dialog.getResult() instanceof Type) {
						controller.setAppareil(
								((Type) dialog.getResult()).getClasse()
										.getConstructor(DeviceModel.class,
												JeuContr�le.class)
										.newInstance(model, controller),
								false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		entrances = Entrances.listForNone();
		exits = Exits.listForNone();

		// Todo : add behaviour
	}

	@Override
	public void action(Stock resATraiter) {
	}

}

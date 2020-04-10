package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.Main;
import com.martin.model.Coordonnees;
import com.martin.model.Stock;
import com.martin.model.appareils.orientation.Entr�es;
import com.martin.model.appareils.orientation.Sorties;
import com.martin.view.JeuContr�le;
import com.martin.view.SolContr�le;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class Appareil_Sol extends Appareil {

	public Appareil_Sol(AppareilModel model, JeuContr�le controller)
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
							new Coordonnees(model.getCoordonnees().getX(),
									model.getCoordonnees().getY()),
							dialog);

					dialog.showAndWait();

					if (dialog.getResult() instanceof Type) {
						controller.setAppareil(
								((Type) dialog.getResult()).getClasse()
										.getConstructor(AppareilModel.class,
												JeuContr�le.class)
										.newInstance(model, controller),
								false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		entrances = Entr�es.listForNone();
		exits = Sorties.listForNone();

		// Todo : add behaviour
	}

	@Override
	public void action(Stock resATraiter) {
	}

}

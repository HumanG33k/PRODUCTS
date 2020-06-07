package com.martinheywang.model.devices;

import java.io.FileNotFoundException;

import com.martinheywang.Main;
import com.martinheywang.model.Coordinates;
import com.martinheywang.model.Pack;
import com.martinheywang.model.database.Database;
import com.martinheywang.model.devices.Template.PointerTypes;
import com.martinheywang.model.devices.Template.TemplateModel;
import com.martinheywang.view.FloorController;
import com.martinheywang.view.GameController;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class Floor extends Device {

	private static TemplateModel templateModel = new TemplateModel(
			PointerTypes.NONE, PointerTypes.NONE, PointerTypes.NONE,
			PointerTypes.NONE);

	public Floor(DeviceModel model, GameController controller)
			throws FileNotFoundException {
		super(model, controller);

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(
							getClass().getResource("/Floor.fxml"));

					Dialog<Object> dialog;
					DialogPane dialogPane;

					dialogPane = (DialogPane) loader.load();
					dialog = new Dialog<Object>();
					dialog.setTitle("Sélectionnez un appareil à construire");
					dialog.setDialogPane(dialogPane);
					dialog.initOwner(Main.getMainStage());
					dialog.initModality(Modality.NONE);

					FloorController SController = loader.getController();
					SController.setMainApp(
							new Coordinates(model.getCoordinates().getX(),
									model.getCoordinates().getY()),
							dialog);

					dialog.showAndWait();

					if (dialog.getResult() instanceof Type) {
						DeviceModel newModel = new DeviceModel(
								model.getCoordinates(),
								model.getGame(),
								(Type) dialog
										.getResult(),
								Level.LEVEL_1,
								Direction.UP);
						Database.createDao(DeviceModel.class).delete(model);
						Database.createDao(DeviceModel.class).create(newModel);
						controller.setAppareil(
								((Type) dialog.getResult()).getClasse()
										.getConstructor(DeviceModel.class,
												GameController.class)
										.newInstance(
												newModel,
												controller),
								false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		template = templateModel.createTemplate(model.getCoordinates(),
				model.getDirection());
	}

	@Override
	public void action(Pack resATraiter) {
		// Here just override the method in case this device is asked for
		// action
		// It souldn't be invoked but it does'nt make the thread overflowed.
	}

	@Override
	protected TemplateModel getTemplateModel() {
		return templateModel;
	}

}

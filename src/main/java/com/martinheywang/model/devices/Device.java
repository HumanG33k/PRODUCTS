package com.martinheywang.model.devices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.martinheywang.Main;
import com.martinheywang.model.Coordinate;
import com.martinheywang.model.Pack;
import com.martinheywang.model.database.Deleter;
import com.martinheywang.model.database.Saver;
import com.martinheywang.model.devices.Template.PointerTypes;
import com.martinheywang.model.devices.Template.TemplateModel;
import com.martinheywang.model.devices.behaviours.Behaviour;
import com.martinheywang.model.exceptions.MoneyException;
import com.martinheywang.toolbox.Tools;
import com.martinheywang.view.DeviceController;
import com.martinheywang.view.GameController;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Duration;

/**
 * The <em>Device</em> is an abstract class that all the devices
 * extends. In the JavaFX scene, it is an ImageView. In the game, it
 * corresponds to each square in the main GridPane.
 * 
 * @author Martin Heywang
 */
public abstract class Device extends ImageView {

	/**
	 * The DeviceModel that contains all the data specified about this
	 * device. Persistent object.
	 * 
	 * @see DeviceModel
	 */
	protected DeviceModel model;

	/**
	 * The Behaviour is basically his behaviour that runs each iterations
	 * of the game loop.
	 * 
	 * @see GameController
	 * 
	 * @see Behaviour
	 */
	protected Behaviour behaviour;

	/**
	 * The controller is useful when you want to change somme data about
	 * the game.
	 * 
	 * @see GameController
	 */
	protected GameController controller;

	/**
	 * The template is a data object who gives all the pointers of this
	 * device, and can indicates which type of connection it is.
	 * 
	 * It is good practice to create first a private static TemplateModel
	 * :
	 * 
	 * <pre>
	 * <code>
	 * private static TemplateModel templateModel = new TemplateModel(PointerTypes.NONE, ...);
	 * </code>
	 * </pre>
	 * 
	 * Four values of type PointerTypes must be given to the contructor :
	 * four PointerTypes values that indicates which type of connection it
	 * is, respectively for the top, then right, then bottom, and to
	 * finish left. This way you can easily create the template, invoking
	 * {@link TemplateModel#createTemplate(Coordinate, Direction) this
	 * method} in the constructor of the device, like this :
	 * 
	 * <pre>
	 * <code>
	 * template = templateModel.createTemplate(model.getCoordinates(), model.getDirection());
	 * </code>
	 * </pre>
	 * 
	 * where <em>model</em> is the current model of the device.
	 * 
	 * <em>Here is an example with the conveyor :</em>
	 * 
	 * <pre>
	 * <code>
	 * // As a field
	 * private static final TemplateModel templateModel = 
	 * 	new TemplateModel(
	 * 		PointerTypes.ENTRY, 
	 * 		PointerTypes.NONE, 
	 * 		PointerTypes.EXIT, 
	 * 		PointerTypes.NONE);
	 * 		
	 *		
	 * // Later in the constructor
	 * template = templateModel.createTemplate(model.getCoordinates(),
	 *	model.getDirection());
	 *	
	 * </code>
	 * </pre>
	 * 
	 * @see Template
	 */
	protected Template template;

	/**
	 * This Timeline field represents the animation is invoked when his
	 * action is invoked. When overriding <code>Device.action</code>,
	 * don't forget do play this animation.
	 */
	protected Timeline timeline;

	/**
	 * What is did in case the user clicks on the Device.
	 */
	private EventHandler<MouseEvent> onClicked = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				openDialog();
			} else if (event.getButton().equals(MouseButton.SECONDARY)) {
				rotate();
			}
		}
	};

	public static int electricity = 5;

	/**
	 * Creates a new device.
	 * 
	 * @param xy         the coordinates of this device
	 * @param type       the kind of the device
	 * @param direction  the rotate of this device
	 * @param niveau     the level of this device
	 * @param controller the game controller
	 * 
	 */
	protected Device(DeviceModel model, GameController controller)
			throws FileNotFoundException {

		this.setImage(new Image(
				getClass().getResourceAsStream(
						"/images" + model.getLevel().getURL()
								+ model.getType().getURL())));
		this.model = model;
		this.controller = controller;

		initDefaultAppearance();
		initActiveAnimation();
		addHoverEffect();
		this.setOnMouseClicked(onClicked);
	}

	private void initActiveAnimation() {
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0),
				new KeyValue(this.opacityProperty(), 1)));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(750),
				new KeyValue(this.opacityProperty(), 1)));
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
				new KeyValue(this.opacityProperty(), 0.7)));
	}

	private void initDefaultAppearance() {
		this.setRotate(model.getDirection().getRotate());
		this.setOpacity(0.7);
	}

	private void openDialog() {
		try {
			final FXMLLoader loader = Tools.prepareFXMLLoader("Device");
			final Dialog<?> dialog = new Dialog<Void>();
			final DialogPane dialogPane;

			dialogPane = (DialogPane) loader.load();
			dialog.setTitle("Tableau de bord - PRODUCTS.");
			dialog.setDialogPane(dialogPane);
			dialog.initOwner(Main.getMainStage());
			dialog.initModality(Modality.NONE);

			final DeviceController controller = loader.getController();
			controller.setContent(this, dialog);
			controller.addActions(getWidgets());

			dialog.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns the list of the widgets of the devices. By
	 * default, it returns nothing. That's why you will need to ovverride
	 * it if you need to add additionnal actions components to the dialog.
	 * Those may have some behaviors on click (event listeners) to add
	 * some features to the device.
	 * 
	 * @return the list of the widgets
	 */
	protected List<Node> getWidgets() {
		return new ArrayList<Node>();
	}

	private void addHoverEffect() {
		this.setOnMouseEntered(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final Node hovered = (Node) event.getSource();
						Main.getMainStage().getScene()
								.setCursor(Cursor.HAND);
						hovered.setEffect(new Glow(0.4d));
					}
				});
		this.setOnMouseExited(
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final Node exited = (Node) event.getSource();
						Main.getMainStage().getScene()
								.setCursor(Cursor.DEFAULT);
						exited.setEffect(new Glow(0d));
					}
				});
	}

	/**
	 * This method represents the behaviour of this device. When called,
	 * it calls the defined behaviour.<br>
	 * <strong>Note :</strong> since most of the device has no longer more
	 * than 1 exit, this method is like a <em>shorcut</em> for <em>simple
	 * devices</em>.It only works if the devices has 1 exit, neither less
	 * or more. In those cases, this method <strong>must be</strong>
	 * overriden to make the device work properly.
	 * 
	 * @param resATraiter the resource who will be used by this device
	 */
	public void action(Pack resATraiter) throws MoneyException {
		for (Coordinate xy : template.getPointersFor(PointerTypes.EXIT)) {
			if (xy.isInGrid(controller.getGridSize())) {
				final Device pointedDevice = controller.findDevice(xy);
				for (Coordinate enter : pointedDevice.getTemplate()
						.getPointersFor(PointerTypes.ENTRY)) {
					if (enter.getX() == model.getCoordinates().getX() &&
							enter.getY() == model.getCoordinates().getY()) {
						behaviour.action(resATraiter,
								template.getPointersFor(PointerTypes.EXIT)
										.get(0));
					}
				}
			}
		}
	}

	/**
	 * Mark this device as activated. This action is only visual, it just
	 * make the device glowing a little bit.
	 */
	public void activate() {
		timeline.stop();
		timeline.playFromStart();
	}

	/**
	 * Upgrades the device by 1 if the level isn't already at maximum.
	 */
	public final void upgrade() throws MoneyException {
		try {
			DeviceModel newModel = new DeviceModel(
					model.getCoordinates(),
					model.getGame(),
					model.getType(),
					model.getLevel().getNext(),
					model.getDirection());
			newModel.setID(model.getID());

			controller.build(this.getClass()
					.getConstructor(DeviceModel.class, GameController.class)
					.newInstance(newModel, controller), false);

			Saver.saveDeviceModel(newModel);
		} catch (SQLException e) {
			controller.toast(
					"L'appareil ne semble pas s'être amélioré correctement...",
					Color.DARKRED, 10d);
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println(
					"Each class that has as superclass Device MUST have a constructor <init>(DeviceModel, GameController).");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void rotate() {

		model.setDirection(model.getDirection().getNext());
		this.setRotate(model.getDirection().getRotate());
		this.setTemplate(
				this.getModel().getType().getTemplateModel().createTemplate(
						model.getCoordinates(),
						model.getDirection()));
		try {
			Saver.saveDeviceModel(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes this device and replaces it by a {@link Floor}.
	 */
	public final void delete() throws MoneyException {
		try {
			controller.delete(model.getCoordinates(), false);
			Deleter.deleteDeviceModel(model);
		} catch (SQLException e) {
			controller.toast(
					"L'appareil ne semble pas s'être détruit correctement...",
					Color.DARKRED, 10d);
			e.printStackTrace();
		}

	}

	public DeviceModel getModel() {
		return model;
	}

	/**
	 * @return the comportement
	 */
	public Behaviour getComportement() {
		return behaviour;
	}

	/**
	 * @return the controller
	 */
	public GameController getController() {
		return controller;
	}

	/**
	 * 
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * @return the electricity
	 */
	public static int getElectricity() {
		return electricity;
	}

	/**
	 * @param behaviour the comportement to set
	 */
	public void setComportement(Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(GameController controller) {
		this.controller = controller;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	/**
	 * @param electricity the electriciy to set
	 */
	public static void setElectricity(int electricity) {
		Device.electricity = electricity;
	}
}

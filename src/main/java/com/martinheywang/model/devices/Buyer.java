package com.martinheywang.model.devices;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martinheywang.model.Coordinates;
import com.martinheywang.model.Pack;
import com.martinheywang.model.BaseResources;
import com.martinheywang.model.devices.Template.PointerTypes;
import com.martinheywang.model.devices.Template.TemplateModel;
import com.martinheywang.model.devices.behaviours.Buyer_;
import com.martinheywang.view.Carousel;
import com.martinheywang.view.Carousel.CarouselEvent;
import com.martinheywang.view.Displayer;
import com.martinheywang.view.GameController;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Buyer extends Device {

	public static ArrayList<Coordinates> locations = new ArrayList<Coordinates>();
	private static TemplateModel templateModel = new TemplateModel(
			PointerTypes.NONE, PointerTypes.NONE, PointerTypes.EXIT,
			PointerTypes.NONE);

	public Buyer(DeviceModel model, GameController controller)
			throws FileNotFoundException {

		super(model, controller);
		locations.add(model.getCoordinates());

		template = templateModel.createTemplate(model.getCoordinates(),
				model.getDirection());
		behaviour = new Buyer_(model, controller);

		Carousel carousel = new Carousel();
		for (BaseResources res : BaseResources.values()) {
			if (Buyer_.acceptedResources.contains(res))
				carousel.addNodes(
						new Displayer<BaseResources>(res.getDisplayer(), res));
		}
		carousel.setOnSelectionChanged(new EventHandler<CarouselEvent>() {
			@Override
			public void handle(CarouselEvent event) {
				BaseResources res = (BaseResources) ((Displayer<?>) event
						.getNewSelection()).getSubject();
				setDistributedResource(res);
			}
		});
		VBox box = new VBox();
		Label extensionTitle = new Label("Changer la ressource distribuée :");
		box.getChildren().addAll(extensionTitle, carousel);
		dashboard.addNode(box);

	}

	@Override
	protected TemplateModel getTemplateModel() {
		return templateModel;
	}

	/**
	 * 
	 * @return the distributed resource
	 * @throws NullPointerException if the behaviour of this device isn't
	 *                              a buyer
	 */
	public BaseResources getDistributedResource() throws NullPointerException {
		if (behaviour instanceof Buyer_)
			return ((Buyer_) behaviour)
					.getDistributedResource().getRessource();
		return null;
	}

	/**
	 * 
	 * @param res the new value of the property
	 */
	public void setDistributedResource(BaseResources res) {
		if (behaviour instanceof Buyer_) {
			((Buyer_) behaviour)
					.setDistributedResource(new Pack(res, 1));
		}
	}
}
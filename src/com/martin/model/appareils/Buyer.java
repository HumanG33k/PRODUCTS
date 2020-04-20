package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.model.Coordinates;
import com.martin.model.Packing;
import com.martin.model.Resource;
import com.martin.model.Stock;
import com.martin.model.appareils.Template.PointerTypes;
import com.martin.model.appareils.Template.TemplateModel;
import com.martin.model.appareils.comportement.Buyer_;
import com.martin.view.JeuContr�le;

public class Buyer extends Device {

	public static ArrayList<Coordinates> liste = new ArrayList<Coordinates>();
	private static TemplateModel templateModel = new TemplateModel(
			PointerTypes.NONE, PointerTypes.NONE, PointerTypes.EXIT,
			PointerTypes.NONE);

	public Buyer(DeviceModel model, JeuContr�le controller)
			throws FileNotFoundException {

		super(model, controller);
		liste.add(model.getCoordinates());

		// Todo: test behaviour

		template = templateModel.createTemplate(model.getCoordinates(),
				model.getDirection());
	}

	@Override
	public void action(Stock resATraiter) {
	}

	/**
	 * 
	 * @return the distributed resource
	 * @throws NullPointerException if the behaviour of this device isn't
	 *                              a buyer
	 */
	public Resource getRessourceDistribu�e() throws NullPointerException {
		if (behaviour instanceof Buyer_)
			return ((Buyer_) behaviour)
					.getRessourceDistribu�e().getRessource();
		return null;
	}

	/**
	 * 
	 * @param res the new value of the property
	 */
	public void setRessourceDistribu�e(Resource res) {
		if (behaviour instanceof Buyer_) {
			((Buyer_) behaviour)
					.setRessourceDistribu�e(new Packing(res, 1));
		}
	}
}
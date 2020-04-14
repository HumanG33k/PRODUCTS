package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.appareils.orientation.Entrances;
import com.martin.model.appareils.orientation.Exits;
import com.martin.view.JeuContr�le;

public class RightConveyor extends Device {

	public RightConveyor(DeviceModel model,
			JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		entrances = Entrances.listForUp(model.getDirection());
		exits = Exits.listForRight(model.getDirection());

		// Todo: add behaviour
	}
}

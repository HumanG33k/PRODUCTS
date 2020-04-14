package com.martin.model.appareils;

import java.io.FileNotFoundException;

import com.martin.model.appareils.orientation.Entrances;
import com.martin.model.appareils.orientation.Exits;
import com.martin.view.JeuContrôle;

public class Furnace extends Device {

	public Furnace(DeviceModel model, JeuContrôle controller)
			throws FileNotFoundException {
		super(model, controller);

		entrances = Entrances.listForUp(model.getDirection());
		exits = Exits.listForCenter(model.getDirection());

		// Todo : add behaviour
	}
}

package com.martin.model.appareils.comportement;

import com.martin.model.Coordinates;
import com.martin.model.Packing;
import com.martin.model.appareils.Device;
import com.martin.model.appareils.DeviceModel;
import com.martin.model.appareils.Level;
import com.martin.model.exceptions.MoneyException;
import com.martin.view.JeuContr�le;

public class Conveyor_ implements Behaviour {

	private Level level;
	private JeuContr�le controller;

	public Conveyor_(DeviceModel model, JeuContr�le controller) {
		this.level = model.getNiveau();
		this.controller = controller;
	}

	@Override
	public void action(Packing resATraiter, Coordinates pointer)
			throws MoneyException {
		// Xxx : check '>' operator if not working properly
		for (int niveau = 0; niveau < this.level.getNiveau() || resATraiter
				.getQuantity() > niveau; niveau++) {
			if (controller.getPartieEnCours().getArgent() < 5
					+ Device.getElectricity())
				throw new MoneyException(
						"Le comportement d'un appareil "
								+ "n'a pas pu �tre r�alis� car le solde "
								+ "d'argent n'�tait pas assez important.");

			controller.setArgent(Device.getElectricity(), false);
		}
		controller.findDevice(pointer).action(resATraiter);
	}

}

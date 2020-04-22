package com.martin.model.appareils.comportement;

import com.martin.model.Coordinates;
import com.martin.model.Packing;
import com.martin.model.Resource;
import com.martin.model.Stock;
import com.martin.model.appareils.Device;
import com.martin.model.appareils.Level;
import com.martin.model.exceptions.MoneyException;
import com.martin.view.JeuContr�le;

public class Furnace_ implements Behaviour {

	private Coordinates pointer;
	private Level level;
	private JeuContr�le controller;

	public Furnace_(Coordinates xy, Level level,
			int xToAdd, int yToAdd, JeuContr�le controller) {
		this.level = level;
		this.controller = controller;
		this.pointer = new Coordinates(xy.getX() + xToAdd, xy.getY() + yToAdd);
	}

	@Override
	public void action(Stock resATraiter) throws MoneyException {
		final Stock tempoStock = new Stock();

		for (int i = 0; i < this.level.getNiveau(); i++) {
			switch (resATraiter.get(i).getRessource()) {
			case FER:
			case OR:
			case CUIVRE:
			case ARGENT:
			case ALUMINIUM:
				tempoStock.add(new Packing(Resource.valueOf(
						"LINGOT_DE_" + resATraiter.get(i).getRessource()),
						resATraiter.get(i).getQuantit�()));

				controller.setArgent(Device.getElectricity(), false);
				break;
			default:
				break;
			}
		}
		controller.getPartieEnCours().getAppareil(pointer).action(tempoStock);

	}

}
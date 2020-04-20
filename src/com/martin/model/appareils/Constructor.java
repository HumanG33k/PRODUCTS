package com.martin.model.appareils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.martin.model.Resource;
import com.martin.model.appareils.comportement.Constructor_;
import com.martin.view.JeuContr�le;

public class Constructor extends Device {

	ArrayList<Resource> recette = new ArrayList<Resource>();

	public Constructor(DeviceModel model, JeuContr�le controller)
			throws FileNotFoundException {
		super(model, controller);

		// Todo : add behaviour
	}

	public void setProduit(Resource res) throws NullPointerException {
		if (behaviour instanceof Constructor_) {
			((Constructor_) behaviour).setProduit(res);
		}
	}

	public Resource getProduit() throws NullPointerException {
		if (behaviour instanceof Constructor_)
			return ((Constructor_) behaviour).getProduit()
					.getRessource();
		return null;
	}
}

package com.martinheywang.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.martinheywang.Main;
import com.martinheywang.model.Game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Home implements Initializable {

	@FXML
	TextField field;
	@FXML
	ImageView button_icon;

	Main main;

	@FXML
	public void create() {
		if (!field.getText().isEmpty()) {
			try {
				Game game = new Game(field.getText());
				main.initGame(game);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		button_icon.setImage(
				new Image(getClass().getResourceAsStream("/icons/add.png")));
	}

	/**
	 * Sets the object main and initialize the data in the widgets.
	 * 
	 * @param main the object to set
	 */
	public void setMainApp(Main main) {
		this.main = main;
	}

}

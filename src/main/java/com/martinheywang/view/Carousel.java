package com.martinheywang.view;

import java.util.ArrayList;
import java.util.List;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class Carousel extends HBox {

	private List<Node> nodes = new ArrayList<Node>();

	private HBox images = new HBox();

	public Carousel() {
		MaterialDesignIconView left_arrow = new MaterialDesignIconView();
		left_arrow.setGlyphName("ARROW_LEFT");
		left_arrow.setGlyphSize(35.0);
		left_arrow.setFill(Color.WHITE);
		left_arrow.getStyleClass().add("arrow");
		MaterialDesignIconView right_arrow = new MaterialDesignIconView();
		right_arrow.setGlyphName("ARROW_RIGHT");
		right_arrow.setGlyphSize(35.0);
		right_arrow.setFill(Color.WHITE);
		right_arrow.getStyleClass().add("arrow");

		final Button previous = new Button();
		previous.setGraphic(left_arrow);
		previous.getStyleClass().add("carousel-btn");
		final Button next = new Button();
		next.getStyleClass().add("carousel-btn");
		next.setGraphic(right_arrow);

		previous.setOnMouseClicked((event) -> previousElement());
		next.setOnMouseClicked((event) -> nextElement());

		this.getChildren().addAll(previous, images, next);
		this.setMinHeight(150d);
		this.setAlignment(Pos.CENTER);
		Carousel.setHgrow(images, Priority.ALWAYS);
	}

	private void nextElement() {
		if (nodes.size() < 3)
			try {
				throw new Exception(
						"The carousel cannot turn, there aren't enough elements in it");
			} catch (Exception e) {
				e.printStackTrace();
			}
		nodes.add(nodes.size() - 1, nodes.remove(0));
		refresh();
	}

	private void previousElement() {
		if (nodes.size() < 3)
			try {
				throw new Exception(
						"The carousel cannot turn, there aren't enough elements in it");
			} catch (Exception e) {
				e.printStackTrace();
			}
		nodes.add(0, nodes.remove(nodes.size() - 1));
		refresh();
	}

	public void refresh() {
		// If the container is null, instantiate a new Node
		if (images == null)
			images = new HBox();
		images.setAlignment(Pos.CENTER);
		Carousel.setHgrow(images, Priority.ALWAYS);

		// Clear all children of the container
		images.getChildren().clear();
		// Return if there are no Node
		if (this.nodes.size() == 0)
			return;
		// If there are less than three, we just show the first node
		else if (this.nodes.size() < 3)
			images.getChildren().addAll(new Group(), this.nodes.get(0),
					new Group());
		// Else we show only three nodes (the last, the first then the second)
		else
			images.getChildren().addAll(this.nodes.get(this.nodes.size() - 1),
					this.nodes.get(0), this.nodes.get(1));

		// We give the children nice effects
		images.getChildren().get(0).setScaleX(0.7d);
		images.getChildren().get(0).setScaleY(0.7d);
		images.getChildren().get(1).setScaleX(1d);
		images.getChildren().get(1).setScaleY(1d);
		images.getChildren().get(2).setScaleX(0.7d);
		images.getChildren().get(2).setScaleY(0.7d);

		// And we fire a Refresh event
		this.fireEvent(new CarouselEvent(this, images));
	}

	public void addNodes(Node... nodes) {
		for (Node node : nodes) {
			this.nodes.add(node);
			refresh();
		}
	}

	public void removeNode(Node node) {
		this.nodes.remove(node);
		refresh();
	}

	/**
	 * Sets the new EventHandler for the event CarouselEvent, in other
	 * words, what is done when the selection of the carousel is changed.
	 * 
	 * @param newHandler the new handler
	 */
	public void setOnSelectionChanged(EventHandler<CarouselEvent> newHandler) {
		this.addEventFilter(CarouselEvent.REFRESH, newHandler);
	}

	/**
	 * 
	 * @author Heywang
	 * 
	 *         A custom event that fires when the selection changed.
	 *
	 */
	public static class CarouselEvent extends Event {

		private static final long serialVersionUID = -4515993623404437149L;

		public static final EventType<CarouselEvent> REFRESH = new EventType<>(
				Event.ANY, "REFRESH");

		/**
		 * Informations that may need the event handler
		 */
		Node oldSelection;
		Node newSelection;

		public Node getOldSelection() {
			return oldSelection;
		}

		public Node getNewSelection() {
			return newSelection;
		}

		// Construtors
		// I don't need the EventType parameter, because the only valid arg is
		// the SELECTION_CHANGED, so I pass it directly
		public CarouselEvent() {
			super(REFRESH);
		}

		public CarouselEvent(Object source, EventTarget target) {
			super(source, target, REFRESH);

			this.oldSelection = ((HBox) target).getChildren().get(0);
			this.newSelection = ((HBox) target).getChildren().get(1);
		}

	}

}

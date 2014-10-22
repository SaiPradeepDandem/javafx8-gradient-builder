package com.javafx.gradientbuilder.application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Custom button, to resemble like a radio button.
 * 
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public class CustomRadioButton extends Button {

	private SimpleBooleanProperty selected = new SimpleBooleanProperty();

	private StackPane greenButton;
	private StackPane grayButton;

	public CustomRadioButton(String text) {
		super(text);
		greenButton = buildButton();
		grayButton = buildButton();

		getStyleClass().add("custom-radio-btn");
		buildGreenButton();
		buildGrayButton();

		setGraphic(grayButton);
		selected.addListener((ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) -> {
			if (arg2) {
				setGraphic(greenButton);
			} else {
				setGraphic(grayButton);
			}
		});

	}

	private StackPane buildButton() {
		final StackPane sp = new StackPane();
		sp.setPrefHeight(21);
		sp.setPrefWidth(21);
		sp.setAlignment(Pos.CENTER);
		return sp;
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}

	public boolean getSelected() {
		return this.selected.get();
	}

	private void buildGreenButton() {
		Circle c1 = buildCircle(10, "radio-selected-c1");
		Circle c2 = buildCircle(8, "radio-selected-c2");
		Circle c3 = buildCircle(5, "radio-selected-c3");
		greenButton.getChildren().addAll(c1, c2, c3);
	}

	private void buildGrayButton() {
		Circle c1 = buildCircle(10, "radio-unselected-c1");
		Circle c2 = buildCircle(8, "radio-unselected-c2");
		Circle c3 = buildCircle(5, "radio-unselected-c3");
		grayButton.getChildren().addAll(c1, c2, c3);
	}

	private Circle buildCircle(double r, String cls) {
		Circle c = new Circle();
		c.setRadius(r);
		c.getStyleClass().add(cls);
		return c;
	}
}

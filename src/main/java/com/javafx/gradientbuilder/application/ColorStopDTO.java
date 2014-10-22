package com.javafx.gradientbuilder.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * DTO object to hold the Color Code and its percentage value.
 * 
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public class ColorStopDTO {
	private SimpleStringProperty colorCode = new SimpleStringProperty();
	private SimpleIntegerProperty percent = new SimpleIntegerProperty();

	public ColorStopDTO(String colorCode, int percent) {
		super();
		this.colorCode.set(colorCode);
		this.percent.set(percent);
	}

	public ColorStopDTO() {
		super();
	}

	public SimpleStringProperty colorCodeProperty() {
		return colorCode;
	}

	public String getColorCode() {
		return colorCode.get();
	}

	public void setColorCode(String colorCode) {
		this.colorCode.set(colorCode);
	}

	public SimpleIntegerProperty percentProperty() {
		return percent;
	}

	public Integer getPercent() {
		return percent.get();
	}

	public void setPercent(Integer percent) {
		this.percent.set(percent);
	}

}

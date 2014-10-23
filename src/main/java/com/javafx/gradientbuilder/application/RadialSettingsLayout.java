package com.javafx.gradientbuilder.application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Radial Settings layout class to configure the layout.
 * 
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public class RadialSettingsLayout extends AbstractSettingsLayout implements SyntaxConstants {

	// Variables to which the values are binded to. And are used to build the gradient.
	protected SimpleBooleanProperty isFocusAngle = new SimpleBooleanProperty(true);
	protected SimpleIntegerProperty focusAngle = new SimpleIntegerProperty();
	protected SimpleBooleanProperty isFocusDistance = new SimpleBooleanProperty(true);
	protected SimpleIntegerProperty focusDistance = new SimpleIntegerProperty();
	protected SimpleBooleanProperty isCenter = new SimpleBooleanProperty(true);
	protected SimpleIntegerProperty centerX = new SimpleIntegerProperty();
	protected SimpleIntegerProperty centerY = new SimpleIntegerProperty();
	protected SimpleBooleanProperty isRadiusPixel = new SimpleBooleanProperty();
	protected SimpleIntegerProperty radiusPixel = new SimpleIntegerProperty();
	protected SimpleIntegerProperty radiusPercent = new SimpleIntegerProperty();

	/**
	 * Constructor to configure the layout.
	 * 
	 * @param app
	 *            - GradientBuilderApp
	 */
	public RadialSettingsLayout(GradientBuilderApp app) {
		super();
		this.app = app;
		this.grid = new GridPane();
		this.grid.setVgap(10);
		setMinWidth(600);
		setPrefWidth(600);

		// Calling the method to add the listener to all the observable properties.
		addListeners();

		// Calling the method to configure the layout.
		configure();
	}

	/**
	 * Method to configure the common listener to all the observable properties. Any change in one value will fire the listener and builds
	 * the gradient and apply the styles to the shapes.
	 */
	private void addListeners() {
		focusAngle.addListener(changeListener);
		focusDistance.addListener(changeListener);
		centerX.addListener(changeListener);
		centerY.addListener(changeListener);
		isRadiusPixel.addListener(changeListener);
		radiusPixel.addListener(changeListener);
		radiusPercent.addListener(changeListener);
		repeatReflect.addListener(changeListener);

		isFocusAngle.addListener(changeListener);
		isFocusDistance.addListener(changeListener);
		isCenter.addListener(changeListener);
		isRepeat.addListener(changeListener);
	}

	/**
	 * Configures the settings layout.
	 */
	protected void configure() {
		super.configure();

		int rowIndex = 0;
		/* Focus Angle */
		CheckBox focusAngleCB = new CheckBox();
		focusAngleCB.selectedProperty().bindBidirectional(isFocusAngle);
		SliderTextField focusAngleField = new SliderTextField(0, 360, 0, "deg");
		focusAngleField.sliderDisableProperty().bind(focusAngleCB.selectedProperty().not());
		focusAngle.bindBidirectional(focusAngleField.valueProperty());

		this.grid.add(focusAngleCB, 0, rowIndex);
		this.grid.add(new Label("Focus Angle : "), 1, rowIndex);
		this.grid.add(focusAngleField, 2, rowIndex, 2, 1);
		rowIndex++;

		/* Focus Distance */
		CheckBox focusDistCB = new CheckBox();
		focusDistCB.selectedProperty().bindBidirectional(isFocusDistance);
		SliderTextField focusDistField = new SliderTextField(-120, 120, 0, "%");
		focusDistField.sliderDisableProperty().bind(focusDistCB.selectedProperty().not());
		focusDistance.bindBidirectional(focusDistField.valueProperty());

		this.grid.add(focusDistCB, 0, rowIndex);
		this.grid.add(new Label("Focus Distance : "), 1, rowIndex);
		this.grid.add(focusDistField, 2, rowIndex, 2, 1);
		rowIndex++;

		/* Center */
		CheckBox centerCB = new CheckBox();
		centerCB.selectedProperty().bindBidirectional(isCenter);
		SliderTextField centerXField = new SliderTextField(-120, 120, 50, "%");
		centerXField.sliderDisableProperty().bind(centerCB.selectedProperty().not());
		centerX.bindBidirectional(centerXField.valueProperty());

		this.grid.add(centerCB, 0, rowIndex);
		this.grid.add(new Label("Center : "), 1, rowIndex);
		this.grid.add(new Label("X : "), 2, rowIndex);
		this.grid.add(centerXField, 3, rowIndex);
		rowIndex++;

		SliderTextField centerYField = new SliderTextField(-120, 120, 50, "%");
		centerYField.sliderDisableProperty().bind(centerCB.selectedProperty().not());
		centerY.bindBidirectional(centerYField.valueProperty());

		this.grid.add(new Label("Y : "), 2, rowIndex);
		this.grid.add(centerYField, 3, rowIndex);
		rowIndex++;

		/* Radius */
		final SliderTextField radiusPercentField = new SliderTextField(0, 120, 50, "%");
		radiusPercent.bindBidirectional(radiusPercentField.valueProperty());

		final SliderTextField radiusPixelField = new SliderTextField(0, 300, 100, "px");
		radiusPixel.bindBidirectional(radiusPixelField.valueProperty());

		final StackPane radiusContainer = new StackPane();
		radiusContainer.setAlignment(Pos.TOP_LEFT);

		ToggleGroup grp = new ToggleGroup();
		RadioButton percentBtn = new RadioButton("Percentage");
		percentBtn.setId("per");
		percentBtn.setToggleGroup(grp);

		RadioButton pixelBtn = new RadioButton("Pixel");
		pixelBtn.setId("pix");
		pixelBtn.setToggleGroup(grp);

		radiusPercentField.disableProperty().bind(pixelBtn.selectedProperty());
		radiusPixelField.disableProperty().bind(percentBtn.selectedProperty());
		grp.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) -> {
			RadioButton btn = (RadioButton) arg2;
			radiusContainer.getChildren().clear();
			if (btn.getId().equals("per")) {
				isRadiusPixel.set(false);
				radiusContainer.getChildren().add(radiusPercentField);
			} else {
				isRadiusPixel.set(true);
				radiusContainer.getChildren().add(radiusPixelField);
			}
		});
		grp.selectToggle(percentBtn);

		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.setSpacing(10);
		hb.getChildren().addAll(percentBtn, pixelBtn);
		this.grid.add(new Label("Radius : "), 1, rowIndex);
		this.grid.add(hb, 2, rowIndex, 2, 1);
		rowIndex++;

		this.grid.add(radiusContainer, 2, rowIndex, 2, 1);
		rowIndex++;

		/* Repeat Or Reflect */
		CheckBox repeatCB = new CheckBox();
		repeatCB.selectedProperty().bindBidirectional(isRepeat);
		ChoiceBox<RepeatOrReflect> repeatChoice = new ChoiceBox<RepeatOrReflect>();
		repeatChoice.disableProperty().bind(repeatCB.selectedProperty().not());
		repeatChoice.setItems(RepeatOrReflect.getList());
		repeatChoice.getSelectionModel().select(0);
		repeatReflect.bind(repeatChoice.getSelectionModel().selectedItemProperty());

		this.grid.add(repeatCB, 0, rowIndex);
		this.grid.add(new Label("Repeat or Reflect : "), 1, rowIndex);
		this.grid.add(repeatChoice, 2, rowIndex, 2, 1);
		rowIndex++;

		/* Color Stops */
		StackPane stopLblPane = new StackPane();
		stopLblPane.setAlignment(Pos.TOP_LEFT);
		stopLblPane.setPadding(new Insets(5, 0, 0, 0));
		stopLblPane.getChildren().add(new Label("Color Stops : "));

		colorStopsVB = new VBox();
		colorStopsVB.setSpacing(15);
		colorStopsVB.getChildren().addAll(getColorStopTemplate(0, 100, 0, -1, "#ffe4c4"), getColorStopTemplate(0, 100, 0, -1, "#d2691e"));

		this.grid.add(stopLblPane, 1, rowIndex);
		this.grid.add(colorStopsVB, 2, rowIndex, 2, 1);
		rowIndex++;

		checkForDeleteBtn();

		ColumnConstraints c1 = new ColumnConstraints();
		c1.setHalignment(HPos.LEFT);
		c1.setMinWidth(20);
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setHalignment(HPos.LEFT);
		c2.setMinWidth(110);
		this.grid.getColumnConstraints().addAll(c1, c2);
	}

	/**
	 * Method to build the final gradient string from the observable properties., and apply it on the shapes.
	 */
	public void buildGradient() {
		StringBuilder sytx = new StringBuilder(bgRadial);

		// Focus Angle
		if (isFocusAngle.get()) {
			sytx.append(focusAngleStart).append(focusAngle.get()).append(focusAngleUnit);
			sytx.append(separator);
		}

		// Focus Distant
		if (isFocusDistance.get()) {
			sytx.append(focusDistStart).append(focusDistance.get()).append(focusDistUnit);
			sytx.append(separator);
		}

		// Center
		if (isCenter.get()) {
			sytx.append(centerStart).append(centerX.get()).append(centerUnit);
			sytx.append(centerY.get()).append(centerUnit);
			sytx.append(separator);
		}

		// Radius
		if (isRadiusPixel.get()) {
			sytx.append(radiusStart).append(radiusPixel.get()).append(radiusPixelUnit);
			sytx.append(separator);
		} else {
			sytx.append(radiusStart).append(radiusPercent.get()).append(radiusPercentUnit);
			sytx.append(separator);
		}

		// Repeat or Reflect
		if (isRepeat.get() && repeatReflect.getValue() != null && !repeatReflect.getValue().equals(RepeatOrReflect.NONE)) {
			sytx.append(repeatReflect.getValue().toString());
			sytx.append(separator);
		}

		// Color Stops
		ColorStopDTO dto;
		for (int i = 0; i < colorStops.size(); i++) {
			dto = colorStops.get(i);
			if (dto.getColorCode() != null && !dto.getColorCode().equals("")) {
				if (dto.getColorCode().indexOf("#") == 0) {
					sytx.append(dto.getColorCode());
				} else {
					sytx.append("#" + dto.getColorCode());
				}

				if (dto.getPercent() > 0) {
					sytx.append(spacer).append(dto.getPercent()).append(colorStopUnit);
				}

				if (i < (colorStops.size() - 1)) {
					sytx.append(separator);
				}
			}
		}

		sytx.append(bgGradEnd);
		gradientSyntax.set(sytx.toString());

		// Setting the result style to nodes.
		app.applyStyles(gradientSyntax.get());
	}
}

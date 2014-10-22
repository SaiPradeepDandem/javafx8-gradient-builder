package com.javafx.gradientbuilder.application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioButtonBuilder;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.RowConstraintsBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;

/**
 * Linear Settings layout class to configure the layout.
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public class LinearSettingsLayout extends AbstractSettingsLayout implements SyntaxConstants{

	// Variables to which the values are binded to. And are used to build the gradient.
	protected SimpleBooleanProperty isFrom = new SimpleBooleanProperty();
	protected SimpleBooleanProperty isFromPixel = new SimpleBooleanProperty();
	protected SimpleIntegerProperty fromXPixel = new SimpleIntegerProperty();
	protected SimpleIntegerProperty fromYPixel = new SimpleIntegerProperty();
	protected SimpleIntegerProperty fromXPercent = new SimpleIntegerProperty();
	protected SimpleIntegerProperty fromYPercent = new SimpleIntegerProperty();
	
	protected SimpleBooleanProperty isTo = new SimpleBooleanProperty(true);
	protected SimpleIntegerProperty toXPixel = new SimpleIntegerProperty();
	protected SimpleIntegerProperty toYPixel = new SimpleIntegerProperty();
	protected SimpleIntegerProperty toXPercent = new SimpleIntegerProperty();
	protected SimpleIntegerProperty toYPercent = new SimpleIntegerProperty();
	protected SimpleObjectProperty<LinearDirection> toDirection = new SimpleObjectProperty<LinearDirection>();
	
	// Instance variables used for building gradient.
	int rowIndex =0;
	ToggleGroup grp;
	StackPane fromContainer;
	StackPane toContainer;
	VBox fromPercentLayout;
	VBox fromPixelLayout;
	VBox toPercentLayout;
	VBox toPixelLayout;
	ChoiceBox<LinearDirection> toChoice;
	CheckBox toCB;
	
	/**
	 * Constructor to configure the layout.
	 * @param app - GradientBuilderApp
	 */
	public LinearSettingsLayout(GradientBuilderApp app){
		super();
		this.app = app;
		this.grid = new GridPane();
		this.grid.setVgap(10);
		
		// Calling the method to add the listener to all the observable properties.
		addListeners();
		
		// Calling the method to configure the layout.
		configure();
	}
	
	/**
	 * Method to configure the common listener to all the observable properties.
	 * Any change in one value will fire the listener and builds the gradient and apply the styles to the shapes.
	 */
	private void addListeners() {
		isFrom.addListener(changeListener);
		isFromPixel.addListener(changeListener);
		fromXPixel.addListener(changeListener);
		fromYPixel.addListener(changeListener);
		fromXPercent.addListener(changeListener);
		fromYPercent.addListener(changeListener);
		
		isTo.addListener(changeListener);
		toXPixel.addListener(changeListener);
		toYPixel.addListener(changeListener);
		toXPercent.addListener(changeListener);
		toYPercent.addListener(changeListener);
		toDirection.addListener(changeListener);
		
		isRepeat.addListener(changeListener);
		repeatReflect.addListener(changeListener);
	}

	/**
	 * Configures the settings layout.
	 */
	private void configure() {
		/* Output Heading*/
		Label outputHeading = LabelBuilder.create().text("Syntax Output :").styleClass("heading1").build();
		
		/* Output TextArea*/
		TextArea textArea = TextAreaBuilder.create().prefHeight(100).minHeight(60).wrapText(true).build();
		textArea.textProperty().bind(gradientSyntax);
		layout.getChildren().addAll(outputHeading, textArea);
		
		/* Settings Heading*/
		Label settingsHeading = LabelBuilder.create().text("Settings :").styleClass("heading1").build();
		layout.getChildren().addAll(settingsHeading, this.grid);
		
		
		configureFrom();
		configureTo();
		
		// Selecting the percent by default.
		grp.selectToggle(grp.getToggles().get(0));
		isFrom.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean selected) {
				if(selected){
					RadioButton btn = (RadioButton)grp.getSelectedToggle();
					loadContainerByRadio(btn);
					toCB.setDisable(true);
					toCB.setSelected(true);
				}else{
					toContainer.getChildren().clear();
					toContainer.getChildren().add(toChoice);
					toCB.setDisable(false);
				}
			}
		});
		isFrom.set(true);
		isFrom.set(false);
		
		/* Repeat Or Reflect*/
		CheckBox repeatCB = new CheckBox();
		repeatCB.selectedProperty().bindBidirectional(isRepeat);
		ChoiceBox<RepeatOrReflect> repeatChoice = new ChoiceBox<RepeatOrReflect>();
		repeatChoice.disableProperty().bind(repeatCB.selectedProperty().not());
		repeatChoice.setItems(RepeatOrReflect.getList());
		repeatChoice.getSelectionModel().select(0);
		repeatReflect.bind(repeatChoice.getSelectionModel().selectedItemProperty());
		
		this.grid.add(repeatCB, 0, rowIndex);
		this.grid.add(new Label("Repeat or Reflect : "), 1, rowIndex);
		this.grid.add(repeatChoice, 2, rowIndex);
		rowIndex++;
		
		/* Color Stops */
		colorStopsVB = VBoxBuilder.create().spacing(15).build();
		colorStopsVB.getChildren().addAll(getColorStopTemplate(0, 100, 0, -1, "#ffb6c1"),
										  getColorStopTemplate(0, 100, 0, -1, "#ffa500"));
		
		this.grid.add(StackPaneBuilder.create().alignment(Pos.TOP_LEFT).padding(new Insets(5,0,0,0)).children(new Label("Color Stops : ")).build(), 1, rowIndex);
		this.grid.add(colorStopsVB, 2, rowIndex);
		rowIndex++;
		
		checkForDeleteBtn();
		
		this.grid.getColumnConstraints().addAll(ColumnConstraintsBuilder.create().halignment(HPos.LEFT).minWidth(20).build(),
				                                ColumnConstraintsBuilder.create().halignment(HPos.LEFT).minWidth(110).build());
		this.grid.getRowConstraints().addAll(RowConstraintsBuilder.create().valignment(VPos.TOP).build(),
											 RowConstraintsBuilder.create().valignment(VPos.TOP).build(),
											 RowConstraintsBuilder.create().valignment(VPos.TOP).build());
                  
	}

	/**
	 * Configures the "from" parameter layout.
	 */
	private void configureFrom(){
		/* From */
		CheckBox fromCB = new CheckBox();
		fromCB.selectedProperty().bindBidirectional(isFrom);
		
		this.grp = new ToggleGroup();
		RadioButton percentBtn = RadioButtonBuilder.create().id("per").text("Percentage").toggleGroup(this.grp).build();
		RadioButton pixelBtn = RadioButtonBuilder.create().id("pix").text("Pixel").toggleGroup(this.grp).build();
		percentBtn.disableProperty().bind(fromCB.selectedProperty().not());
		pixelBtn.disableProperty().bind(fromCB.selectedProperty().not());
		grp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> arg0,	Toggle arg1, Toggle arg2) {
				RadioButton btn = (RadioButton)arg2;
				loadContainerByRadio(btn);
			}
		});
		
		this.grid.add(fromCB, 0, rowIndex);
		this.grid.add(new Label("From : "), 1, rowIndex);
		this.grid.add(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).spacing(10).children(percentBtn,pixelBtn).build(), 2, rowIndex);
		rowIndex++;
		
		// From Percent Container fields
		Label fromXPercentLabel = LabelBuilder.create().text("X : ").minWidth(20).build();
		SliderTextField fromXPercentField = new SliderTextField(-120, 120, 0, "%");
		fromXPercentField.sliderDisableProperty().bind(fromCB.selectedProperty().not());
		fromXPercent.bindBidirectional(fromXPercentField.valueProperty());
		
		Label fromYPercentLabel = LabelBuilder.create().text("Y : ").minWidth(20).build();
		SliderTextField fromYPercentField = new SliderTextField(-120, 120, 0, "%");
		fromYPercentField.sliderDisableProperty().bind(fromCB.selectedProperty().not());
		fromYPercent.bindBidirectional(fromYPercentField.valueProperty());
		
		fromPercentLayout = new VBox();
		fromPercentLayout.getChildren().addAll(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(fromXPercentLabel,fromXPercentField).build(), 
				                               HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(fromYPercentLabel,fromYPercentField).build());
		
		// From Pixel Container fields
		Label fromXPixelLabel = LabelBuilder.create().text("X : ").minWidth(20).build();
		SliderTextField fromXPixelField = new SliderTextField(-120, 120, 0, "px");
		fromXPixelField.sliderDisableProperty().bind(fromCB.selectedProperty().not());
		fromXPixel.bindBidirectional(fromXPixelField.valueProperty());
		
		Label fromYPixelLabel = LabelBuilder.create().text("Y : ").minWidth(20).build();
		SliderTextField fromYPixelField = new SliderTextField(-120, 120, 0, "px");
		fromYPixelField.sliderDisableProperty().bind(fromCB.selectedProperty().not());
		fromYPixel.bindBidirectional(fromYPixelField.valueProperty());
		
		fromPixelLayout = new VBox();
		fromPixelLayout.getChildren().addAll(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(fromXPixelLabel,fromXPixelField).build(), 
                                             HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(fromYPixelLabel,fromYPixelField).build());

		fromContainer = new StackPane();
		
		this.grid.add(fromContainer, 2, rowIndex);
		rowIndex++;
	}
	
	/**
	 * Configures the "to" parameter layout.
	 */
	private void configureTo(){
		/* To */
		toCB = new CheckBox();
		toCB.selectedProperty().bindBidirectional(isTo);
		toChoice = new ChoiceBox<LinearDirection>();
		toChoice.disableProperty().bind(toCB.selectedProperty().not());
		toChoice.setItems(LinearDirection.getList());
		toChoice.getSelectionModel().select(LinearDirection.BOTTOM);
		toDirection.bind(toChoice.getSelectionModel().selectedItemProperty());
		
		// To Percent Container fields
		Label toXPercentLabel = LabelBuilder.create().text("X : ").minWidth(20).build();
		SliderTextField toXPercentField = new SliderTextField(-120, 120, 50, "%");
		toXPercentField.sliderDisableProperty().bind(isFrom.not());
		toXPercent.bindBidirectional(toXPercentField.valueProperty());
		
		Label toYPercentLabel = LabelBuilder.create().text("Y : ").minWidth(20).build();
		SliderTextField toYPercentField = new SliderTextField(-120, 120, 50, "%");
		toYPercentField.sliderDisableProperty().bind(isFrom.not());
		toYPercent.bindBidirectional(toYPercentField.valueProperty());
		
		toPercentLayout = new VBox();
		toPercentLayout.getChildren().addAll(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(toXPercentLabel,toXPercentField).build(), 
				                               HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(toYPercentLabel,toYPercentField).build());
		
		// To Pixel Container fields
		Label toXPixelLabel = LabelBuilder.create().text("X : ").minWidth(20).build();
		SliderTextField toXPixelField = new SliderTextField(0, 300, 50, "px");
		toXPixelField.sliderDisableProperty().bind(isFrom.not());
		toXPixel.bindBidirectional(toXPixelField.valueProperty());
		
		Label toYPixelLabel = LabelBuilder.create().text("Y : ").minWidth(20).build();
		SliderTextField toYPixelField = new SliderTextField(0, 300, 50, "px");
		toYPixelField.sliderDisableProperty().bind(isFrom.not());
		toYPixel.bindBidirectional(toYPixelField.valueProperty());
		
		toPixelLayout = new VBox();
		toPixelLayout.getChildren().addAll(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(toXPixelLabel,toXPixelField).build(), 
                                             HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(toYPixelLabel,toYPixelField).build());

				
		toContainer = StackPaneBuilder.create().alignment(Pos.TOP_LEFT).build();
		
		this.grid.add(toCB, 0, rowIndex);
		this.grid.add(new Label("To : "), 1, rowIndex);
		this.grid.add(toContainer, 2, rowIndex);
		rowIndex++;
		
	}
	
	private void loadContainerByRadio(RadioButton btn){
		fromContainer.getChildren().clear();
		toContainer.getChildren().clear();
		if(btn.getId().equals("per")){
			isFromPixel.set(false);
			fromContainer.getChildren().add(fromPercentLayout);
			toContainer.getChildren().add(toPercentLayout);
		}else{
			isFromPixel.set(true);
			fromContainer.getChildren().add(fromPixelLayout);
			toContainer.getChildren().add(toPixelLayout);
		}
	}
	
	/**
	 * Method to build the final gradient string from the observable properties.,
	 * and apply it on the shapes.
	 */
	public void buildGradient() {
		StringBuilder sytx = new StringBuilder(bgLinear);
		
		// From
		if(isFrom.get()){
			if(isFromPixel.get()){
				sytx.append(from);
				sytx.append(fromXPixel.get()).append(fromPixelUnit);
				sytx.append(fromYPixel.get()).append(fromPixelUnit);
				sytx.append(to);
				sytx.append(toXPixel.get()).append(toPixelUnit);
				sytx.append(toYPixel.get()).append(toPixelUnit);
				sytx.append(separator);
				
			}else{
				sytx.append(from);	
				sytx.append(fromXPercent.get()).append(fromPercentUnit);
				sytx.append(fromYPercent.get()).append(fromPercentUnit);
				sytx.append(to);
				sytx.append(toXPercent.get()).append(toPercentUnit);
				sytx.append(toYPercent.get()).append(toPercentUnit);
				sytx.append(separator);
			}
		}
		// To
		else if(isTo.get() && toDirection.getValue()!=null){
			sytx.append(to);
			sytx.append(toDirection.getValue().toString());
			sytx.append(separator);
		}
		
		// Repeat or Reflect
		if(isRepeat.get() && repeatReflect.getValue()!=null && !repeatReflect.getValue().equals(RepeatOrReflect.NONE)){
			sytx.append(repeatReflect.getValue().toString());
			sytx.append(separator);
		}
		
		// Color Stops
		ColorStopDTO dto;
		for (int i=0 ; i<colorStops.size(); i++) {
			dto =colorStops.get(i);
			if(dto.getColorCode()!=null && !dto.getColorCode().equals("")){
				if(dto.getColorCode().indexOf("#") == 0){
					sytx.append(dto.getColorCode());
				}else{
					sytx.append("#"+dto.getColorCode());
				}
						
				if(dto.getPercent()>0){
					sytx.append(spacer).append(dto.getPercent()).append(colorStopUnit);
				}
				
				if(i<(colorStops.size()-1)){
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

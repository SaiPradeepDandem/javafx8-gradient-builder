package com.javafx.gradientbuilder.application;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.javafx.gradientbuilder.application.SyntaxConstants.RepeatOrReflect;

/**
 * Abstract class which contains the common fields for both "Linear Settings layout" and "Radial Settings Layout".
 * 
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public abstract class AbstractSettingsLayout extends StackPane {

	protected GradientBuilderApp app;
	protected SimpleStringProperty gradientSyntax = new SimpleStringProperty("");

	protected SimpleBooleanProperty isRepeat = new SimpleBooleanProperty(true);
	protected SimpleObjectProperty<RepeatOrReflect> repeatReflect = new SimpleObjectProperty<RepeatOrReflect>();
	protected ObservableList<ColorStopDTO> colorStops = FXCollections.observableArrayList();

	protected VBox layout;
	protected VBox colorStopsVB;
	protected GridPane grid;

	// Listener to build the gradient on change of value.
	protected ChangeListener<Object> changeListener = (ObservableValue<? extends Object> arg0, Object arg1, Object arg2) -> {
		buildGradient();
	};

	/**
	 * Constructor to initialize the layout.
	 */
	public AbstractSettingsLayout() {
		super();
		setAlignment(Pos.TOP_LEFT);
		layout = new VBox();
		layout.setSpacing(10);
		layout.setPadding(new Insets(10));
		getChildren().add(layout);
	}

	protected abstract void buildGradient();

	/**
	 * Returns the color stop row template.
	 * 
	 * @param startValue
	 *            - Start value of the slider.
	 * @param endValue
	 *            - End value of the slider.
	 * @param pos
	 *            - Default value of the slider
	 * @param finalPos
	 *            - Position to which the color stop need to be added.
	 * @param color
	 *            - Color code to be set in the text field.
	 * @return HBox
	 */
	protected HBox getColorStopTemplate(int startValue, int endValue, int pos, int finalPos, String color) {
		HBox hb = getColorStopTemplate(startValue, endValue, pos, finalPos);
		((ColorPicker) hb.getChildren().get(0)).setValue(Color.web(color));
		return hb;
	}

	/**
	 * Returns the color stop row template.
	 * 
	 * @param startValue
	 *            - Start value of the slider.
	 * @param endValue
	 *            - End value of the slider.
	 * @param pos
	 *            - Default value of the slider
	 * @param finalPos
	 *            - Position to which the color stop need to be added.
	 * @return HBox
	 */
	protected HBox getColorStopTemplate(int startValue, int endValue, int pos, int finalPos) {
		ColorStopDTO dto = new ColorStopDTO();
		dto.colorCodeProperty().addListener(changeListener);
		dto.percentProperty().addListener(changeListener);

		if (finalPos == -1) {
			colorStops.add(dto);
		} else {
			colorStops.add(finalPos, dto);
		}

		SliderTextField sliderTF = new SliderTextField(startValue, endValue, pos);
		dto.percentProperty().bindBidirectional(sliderTF.valueProperty());

		ColorPicker colorPicker = new ColorPicker();
		colorPicker.setPrefWidth(110);
		colorPicker.setMaxWidth(110);
		colorPicker.setMinWidth(110);
		dto.colorCodeProperty().bind(new StringBinding() {
			{
				bind(colorPicker.valueProperty());
			}

			@Override
			protected String computeValue() {
				final Color c = colorPicker.getValue();
				return String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255),
						(int) (c.getBlue() * 255));
			}

		});

		Button addBtn = buildImgButton("add-button");
		Button deleteBtn = buildImgButton("delete-button");

		final HBox hb = new HBox();
		hb.setMaxHeight(30);
		hb.setSpacing(20);
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.getChildren().addAll(colorPicker, sliderTF, addBtn, deleteBtn);

		addBtn.setOnAction((e) -> addNewColorStop(hb));
		deleteBtn.setOnAction((e) -> deleteColorStop(hb));

		return hb;
	}

	private Button buildImgButton(String styleCls) {
		Button button = new Button();
		button.getStyleClass().addAll("transparentButton", styleCls);
		return button;
	}

	/**
	 * Method to add a new ColorStop row in the list and the layout. Called on click of "+" button.
	 * 
	 * @param current
	 *            - Current color template row to determine the position.
	 */
	private void addNewColorStop(HBox current) {
		int finalPos = getColorStopPosition(current);
		finalPos = (finalPos == colorStopsVB.getChildren().size() - 1 || finalPos == -1) ? -1 : (finalPos + 1);
		if (finalPos == -1) {
			colorStopsVB.getChildren().add(getColorStopTemplate(0, 100, 0, finalPos));
		} else {
			colorStopsVB.getChildren().add(finalPos, getColorStopTemplate(0, 100, 0, finalPos));
		}

		// After adding the row calling the method to build the gradient and apply the styles to the shapes.
		buildGradient();

		// Calling the method to enable delete buttons based on the row count.
		checkForDeleteBtn();
	}

	/**
	 * Method to remove the color stop row from the list and the layout. Called on click of "X" button.
	 * 
	 * @param current
	 *            - Current color template row to determine the position.
	 */
	private void deleteColorStop(HBox current) {
		int finalPos = getColorStopPosition(current);

		colorStops.get(finalPos).colorCodeProperty().removeListener(changeListener);
		colorStops.get(finalPos).percentProperty().removeListener(changeListener);
		colorStops.remove(finalPos);

		colorStopsVB.getChildren().remove(current);
		// After adding the row calling the method to build the gradient and apply the styles to the shapes.
		buildGradient();

		// Calling the method to enable delete buttons based on the row count.
		checkForDeleteBtn();
	}

	/**
	 * Utility method to get the color stop position from the list.
	 * 
	 * @param current
	 *            - Current color template row to determine the position.
	 * @return position.
	 */
	protected int getColorStopPosition(HBox current) {
		int finalPos = -1;
		for (int i = 0; i < colorStopsVB.getChildren().size(); i++) {
			if (colorStopsVB.getChildren().get(i) == current) {
				finalPos = i;
				break;
			}
		}
		return finalPos;
	}

	/**
	 * Utility method to check for no of color-stops and enable/disable the delete buttons.
	 */
	protected void checkForDeleteBtn() {
		boolean flag = (colorStopsVB.getChildren().size() > 2) ? true : false;
		for (Node node : colorStopsVB.getChildren()) {
			if (flag) {
				((HBox) node).getChildren().get(3).setDisable(false);
			} else {
				((HBox) node).getChildren().get(3).setDisable(true);
			}
		}
	}
	
	/**
	 * Configures the settings layout.
	 */
	protected void configure() {
		/* Output Heading*/
		Label outputHeading = new Label("Syntax Output :");
		outputHeading.getStyleClass().add("heading1");
		
		/* Output TextArea*/
		TextArea textArea = new TextArea();
		textArea.prefHeight(100);
		textArea.minHeight(60);
		textArea.setWrapText(true);
		textArea.textProperty().bind(gradientSyntax);
		layout.getChildren().addAll(outputHeading, textArea);
		
		/* Settings Heading*/
		Label settingsHeading = new Label("Settings :");
		settingsHeading.getStyleClass().add("heading1");
		layout.getChildren().addAll(settingsHeading, this.grid);
	}

}

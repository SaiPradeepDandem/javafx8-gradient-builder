package com.javafx.gradientbuilder.application;

import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Main class to start the gradient builder.
 *
 * @author <a href="mailto:saipradeep.dandem@gmail.com">Sai Dandem</a>
 *
 */
@SuppressWarnings("restriction")
public class GradientBuilderApp extends Application {

	// Root Node of the application.
	private BorderPane root;
	private BorderPane center;

	// Shapes(Panes) to which the gradient is applied.
	private StackPane rectangle;
	private StackPane circle;

	// Instance Variables
	private enum GradientType {
		LINEAR, RADIAL
	};

	private LinearSettingsLayout linearSettingLayout;
	private RadialSettingsLayout radialSettingLayout;
	private StackPane settingsContainer;

	// Observable Property to determine the type of the current selected gradient.
	private SimpleObjectProperty<GradientType> gradientType = new SimpleObjectProperty<GradientType>();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		configureSceneAndStage(stage);
		configureHeader();
		configureFooter();
		configureCenter();
	}

	/**
	 * Configures the Scene and Stage for the application.
	 */
	private void configureSceneAndStage(Stage stage) {
		// Initializing the root node
		root = new BorderPane();
		root.autosize();
		Scene scene = new Scene(root, Color.WHITE);
		scene.getStylesheets().add("styles/gradientbuilder.css");

		// Default settings for the stage.
		stage.setTitle("Gradient Builder");
		stage.setWidth(1200);
		stage.setHeight(700);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Configures the header of the application.
	 */
	private void configureHeader() {
		ImageView iconImage = new ImageView(new Image(getClass().getResourceAsStream("/images/app-icon.png")));
		iconImage.setFitHeight(80);
		iconImage.setFitWidth(80);

		Text header = new Text("Gradient Builder");
		header.getStyleClass().add("app-header-text");

		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.setSpacing(15);
		hb.setPadding(new Insets(10));
		hb.getChildren().addAll(iconImage, header);

		Bloom bloom = new Bloom();
		bloom.setThreshold(0.3);
		header.setEffect(bloom);

		StackPane background = new StackPane();
		background.getStyleClass().add("app-header");
		background.getChildren().addAll(hb);
		background.setPrefHeight(100);

		root.setTop(background);
	}

	/**
	 * Configures the footer of the application.
	 */
	private void configureFooter() {
		Label footer = new Label("Developed by a JavaFX Enthusiast :)");
		footer.getStyleClass().add("app-footer-text");

		StackPane background = new StackPane();
		background.getStyleClass().add("app-footer");
		background.getChildren().addAll(footer);
		background.setPrefHeight(20);

		root.setBottom(background);
	}

	/**
	 * Configures the center/body of the application.
	 */
	private void configureCenter() {
		center = new BorderPane();
		root.setCenter(center);
		configureBody();
		configureToolBar();
	}

	/**
	 * Configures the center part(body) of the application.
	 */
	private void configureBody() {
		// Getting the left side top pane. (Rectangle's view)
		StackPane rectanglePane = configureRectanglePane();

		// Getting the left side bottom pane. (Circle's view)
		StackPane circlePane = configureCirclePane();

		// Getting the right side settings pane.
		ScrollPane rightPane = configureGradientSettings();

		SplitPane leftPane = new SplitPane();
		leftPane.setOrientation(Orientation.VERTICAL);
		leftPane.getItems().addAll(rectanglePane, circlePane);

		SplitPane mainPane = new SplitPane();
		mainPane.getItems().addAll(leftPane, rightPane);

		// Setting the entire layout as the center to the root(BorderPane) node.
		center.setCenter(mainPane);
	}

	/**
	 * Configures the tool bar of the application.
	 */
	private void configureToolBar() {
		// Getting the "Custom" radio buttons for the Gradient types.
		final CustomRadioButton linearButton = new CustomRadioButton("Linear");
		linearButton.setOnAction((e) -> gradientType.set(GradientType.LINEAR));

		final CustomRadioButton radialButton = new CustomRadioButton("Radial");
		radialButton.setOnAction((e) -> gradientType.set(GradientType.RADIAL));

		// When the gradient type is changed, Listener to switch the layouts and apply the styles to the shapes.
		gradientType.addListener((ObservableValue<? extends GradientType> arg0, GradientType arg1, GradientType type) -> {
			settingsContainer.getChildren().clear();
			switch (type) {
			case LINEAR:
				linearButton.setSelected(true);
				radialButton.setSelected(false);
				settingsContainer.getChildren().add(linearSettingLayout);
				linearSettingLayout.buildGradient();
				break;
			case RADIAL:
				linearButton.setSelected(false);
				radialButton.setSelected(true);
				settingsContainer.getChildren().add(radialSettingLayout);
				radialSettingLayout.buildGradient();
				break;
			}
		});

		// Initializing the application tool bar and setting the radio buttons.
		ToolBar toolBar = new ToolBar();
		toolBar.setPrefHeight(35);
		toolBar.getItems().addAll(linearButton, radialButton);

		// Setting the ToolBar as the top to the root(BorderPane) node.
		center.setTop(toolBar);

		// By default selecting the "Linear" gradient.
		gradientType.set(GradientType.LINEAR);
	}

	/**
	 * Configures the layout for the "Rectangle" shape.
	 * 
	 * @return StackPane
	 */
	private StackPane configureRectanglePane() {
		// Initializing the "Rectangle".
		rectangle = new StackPane();

		// Creating the width label and binding its value with the rectangle's "widthProperty".
		Label widthLbl = getValueLabel();
		widthLbl.textProperty().bind(new StringBinding() {
			{
				bind(rectangle.widthProperty());
			}

			@Override
			protected String computeValue() {
				return rectangle.getWidth() + "px";
			}
		});

		// Creating the height label and binding its value with the rectangle's "heightProperty".
		Label heightLbl = getValueLabel();
		heightLbl.textProperty().bind(new StringBinding() {
			{
				bind(rectangle.heightProperty());
			}

			@Override
			protected String computeValue() {
				return rectangle.getHeight() + "px";
			}
		});

		// Creating a HBox layout to place the Rectangle's width and height information.
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_RIGHT);
		hb.setPrefHeight(20);
		hb.getChildren().addAll(getBoldLabel("Width : "), widthLbl, getSpacer(), getBoldLabel("Height : "), heightLbl);

		// BorderPane to hold the Rectangle and Bounds information.
		BorderPane bp = new BorderPane();
		bp.setCenter(rectangle);
		bp.setBottom(hb);

		// StackPane to hold the BorderPane and to apply the padding to BorderPane.
		StackPane rectanglePane = new StackPane();
		rectanglePane.setPadding(new Insets(15, 15, 3, 15));
		rectanglePane.getChildren().add(bp);
		return rectanglePane;
	}

	/**
	 * Configures the layout for the "Circle" shape.
	 * 
	 * @return StackPane
	 */
	private StackPane configureCirclePane() {
		// Initializing the "Circle".
		circle = new StackPane();
		circle.setAlignment(Pos.CENTER);
		circle.getStyleClass().add("circle-shape");

		// Creating the x-radius label and binding its value with the circle's half "widthProperty".
		Label radiusX = getValueLabel();
		radiusX.textProperty().bind(new StringBinding() {
			{
				bind(circle.widthProperty());
			}

			@Override
			protected String computeValue() {
				return (circle.getWidth()) / 2 + "px";
			}
		});

		// Creating the y-radius label and binding its value with the circle's half "heightProperty".
		Label radiusY = getValueLabel();
		radiusY.textProperty().bind(new StringBinding() {
			{
				bind(circle.heightProperty());
			}

			@Override
			protected String computeValue() {
				return ((circle.getHeight() / 2)) + "px";
			}
		});

		// Creating a HBox layout to place the Circle's x-radius and y-radius information.
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER_RIGHT);
		hb.setPrefHeight(20);
		hb.getChildren().addAll(getBoldLabel("X-Radius : "), radiusX, getSpacer(), getBoldLabel("Y-Radius : "), radiusY);

		// BorderPane to hold the Circle and Bounds information.
		BorderPane bp = new BorderPane();
		bp.setCenter(circle);
		bp.setBottom(hb);

		// StackPane to hold the BorderPane and to apply the padding to BorderPane.
		StackPane circlePane = new StackPane();
		circlePane.setPadding(new Insets(15, 15, 3, 15));
		circlePane.getChildren().add(bp);
		return circlePane;
	}

	/**
	 * Configures the Gradient setting pane.
	 * 
	 * @return ScrollPane
	 */
	private ScrollPane configureGradientSettings() {
		// Initializing the RadialSettingsLayout.
		radialSettingLayout = new RadialSettingsLayout(this);

		// Initializing the LinearSettingsLayout.
		linearSettingLayout = new LinearSettingsLayout(this);

		// Initializing the container to hold RadialSettingsLayout or LinearSettingsLayout.
		settingsContainer = new StackPane();
		settingsContainer.setAlignment(Pos.TOP_LEFT);

		// Wrapping the container with the ScrollPane.
		ScrollPane scroll = new ScrollPane();
		scroll.getStyleClass().add("builder-scroll-pane");
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setContent(settingsContainer);
		return scroll;
	}

	/**
	 * Utility method to return a StackPane with some width to act like a spacer.
	 * 
	 * @return StackPane
	 */
	private StackPane getSpacer() {
		StackPane sp = new StackPane();
		sp.setPrefWidth(20);
		return sp;
	}

	/**
	 * Utility method to return a Label with bold font style.
	 * 
	 * @return Label
	 */
	private Label getBoldLabel(String str) {
		Label lbl = new Label(str);
		lbl.getStyleClass().add("-fx-font-weight:bold;");
		return lbl;
	}

	/**
	 * Utility method to return a Label with normal font style.
	 * 
	 * @return Label
	 */
	private Label getValueLabel() {
		Label lbl = new Label();
		lbl.getStyleClass().add("-fx-font-family:verdana;");
		return lbl;
	}

	/**
	 * Method to apply the styles to the shapes.
	 * 
	 * @param bg
	 *            - CSS gradient string.
	 */
	public void applyStyles(String bg) {
		rectangle.setStyle("-fx-background-color:" + bg);
		circle.setStyle("-fx-background-color:" + bg);
	}
}

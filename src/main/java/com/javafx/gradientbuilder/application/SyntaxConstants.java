package com.javafx.gradientbuilder.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * String constants that are used to build the gradient syntax.
 * @author Sai.Dandem
 *
 */
@SuppressWarnings("restriction")
public interface SyntaxConstants {
	public String spacer=" ";
	public String separator=", ";
	public String bgTxt = "-fx-background-color: ";
	public String bgRadial = "radial-gradient(";
	public String bgLinear = "linear-gradient(";
	public String bgGradEnd = ");";
	public String focusAngleStart = "focus-angle ";
	public String focusAngleUnit = "deg ";
	public String focusDistStart = "focus-distance ";
	public String focusDistUnit = "% ";
	public String centerStart = "center ";
	public String centerUnit = "% ";
	public String radiusStart = "radius ";
	public String radiusPercentUnit = "% ";
	public String radiusPixelUnit = "px ";
	public String repeat = "repeat ";
	public String reflect = "reflect ";
	public String colorStopUnit="% ";
	public String pointPercentUnit = "% ";
	public String pointPixelUnit = "px ";
	public String from ="from ";
	public String fromPixelUnit ="px ";
	public String fromPercentUnit ="% ";
	public String to ="to ";
	public String toPixelUnit ="px ";
	public String toPercentUnit ="% ";
	
	public enum RepeatOrReflect {
		NONE("None"),REPEAT("repeat"), REFLECT("reflect");
		
		String value;
		RepeatOrReflect(String value){
			this.value= value;
		}
		
		public static ObservableList<RepeatOrReflect> getList(){
			ObservableList<RepeatOrReflect> list = FXCollections.observableArrayList();
			list.addAll(RepeatOrReflect.values());
			return list;
		}
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	public enum LinearDirection{
		TOP("top"), LEFT("left"), BOTTOM("bottom"), RIGHT("right"),
		TOP_LEFT("top left"), TOP_RIGHT("top right"),
		BOTTOM_LEFT("bottom left"), BOTTOM_RIGHT("bottom right");
		
		String value;
		LinearDirection(String value){
			this.value= value;
		}
		
		public static ObservableList<LinearDirection> getList(){
			ObservableList<LinearDirection> list = FXCollections.observableArrayList();
			list.addAll(LinearDirection.values());
			return list;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
}

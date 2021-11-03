package nl.gwe.view;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.gwe.controllers.RootController;
import nl.gwe.datalists.Meters;

@Component
public class ChartControlPanel {
	
	private final MonthUsageChartView monthUsageChartView;
	
	private RootController root;

	GridPane panel;
	ToggleGroup meterGroup;
	RadioButton radioLowElectricityPurchased;
	RadioButton radioLowElectricityDelivered;
	RadioButton radioHighElectricityPurchased;
	RadioButton radioHighElectricityDelivered;
	RadioButton radioTotalElectricity;
	RadioButton radioGasPurchased;
	RadioButton radioWaterPurchased;

	public ChartControlPanel(MonthUsageChartView monthUsageChartView) {
		this.monthUsageChartView = monthUsageChartView;
		panel = new GridPane();
		meterGroup = new ToggleGroup();
		panel.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		panel.setPadding(new Insets(10, 10, 10, 10));
		panel.setHgap(5);
		panel.setVgap(5);
        panel.add(createMeterToggleGroupBox(), 0, 0, 1, 6);
//		panel.setGridLinesVisible(true);
	}
	
	public GridPane getPanel(RootController root) {
		this.root = root;
		return panel;
	}
	
//	@FXML
	private void setMeter(ActionEvent actionEvent) {
		radioLowElectricityPurchased.setUserData(Meters.LOW_ELECTRICITY_PURCHASED);
		radioLowElectricityDelivered.setUserData(Meters.LOW_ELECTRICITY_DELIVERED);
		radioHighElectricityPurchased.setUserData(Meters.HIGH_ELECTRICITY_PURCHASED);
		radioHighElectricityDelivered.setUserData(Meters.HIGH_ELECTRICITY_DELIVERED);
		radioTotalElectricity.setUserData(Meters.TOTAL_ELECTRICITY);
		radioGasPurchased.setUserData(Meters.GAS_PURCHASED);
		radioWaterPurchased.setUserData(Meters.WATER_PURCHASED);
		Toggle toggle = meterGroup.getSelectedToggle();
		monthUsageChartView.setMeter((Meters)toggle.getUserData());
		
		// Tricky way to trigger root controller to update the graphical view!
		if (root != null) {
			root.showGraphicalView(actionEvent);
		}
	}
	
	private HBox createMeterToggleGroupBox() {
		radioLowElectricityPurchased = new RadioButton(Meters.LOW_ELECTRICITY_PURCHASED.name());
		radioLowElectricityDelivered = new RadioButton(Meters.LOW_ELECTRICITY_DELIVERED.name());
		radioHighElectricityPurchased = new RadioButton(Meters.HIGH_ELECTRICITY_PURCHASED.name());
		radioHighElectricityDelivered = new RadioButton(Meters.HIGH_ELECTRICITY_DELIVERED.name());
		radioTotalElectricity = new RadioButton(Meters.TOTAL_ELECTRICITY.name());
		radioGasPurchased = new RadioButton(Meters.GAS_PURCHASED.name());
		radioGasPurchased.setSelected(true); // Default view
		radioWaterPurchased = new RadioButton(Meters.WATER_PURCHASED.name());
		
		radioLowElectricityPurchased.setToggleGroup(meterGroup);
		radioLowElectricityDelivered.setToggleGroup(meterGroup);
		radioHighElectricityPurchased.setToggleGroup(meterGroup);
		radioHighElectricityDelivered.setToggleGroup(meterGroup);
		radioTotalElectricity.setToggleGroup(meterGroup);
		radioGasPurchased.setToggleGroup(meterGroup);
		radioWaterPurchased.setToggleGroup(meterGroup);
		
		radioLowElectricityPurchased.setOnAction(this::setMeter);
		radioLowElectricityDelivered.setOnAction(this::setMeter);
		radioHighElectricityPurchased.setOnAction(this::setMeter);
		radioHighElectricityDelivered.setOnAction(this::setMeter);
		radioTotalElectricity.setOnAction(this::setMeter);
		radioGasPurchased.setOnAction(this::setMeter);
		radioWaterPurchased.setOnAction(this::setMeter);
		
		VBox vbox1 = new VBox(radioLowElectricityPurchased,
				radioLowElectricityDelivered,
				radioHighElectricityPurchased,
				radioHighElectricityDelivered,
				radioTotalElectricity
				);
		vbox1.setSpacing(10);
		VBox vbox2 = new VBox(radioGasPurchased,
				radioWaterPurchased);
		vbox2.setSpacing(10);
		HBox hbox = new HBox(vbox1, vbox2);
		hbox.setSpacing(30);
		return hbox;
	}
	
	
}

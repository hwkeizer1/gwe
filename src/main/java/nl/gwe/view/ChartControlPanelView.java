package nl.gwe.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.controllers.RootController;
import nl.gwe.datalists.Meters;
import nl.gwe.datalists.MonthUsageList;
import nl.gwe.domain.Measurement;

@Slf4j
@Component
public class ChartControlPanelView implements ListChangeListener<Integer> {
	
	private final MonthUsageChartView monthUsageChartView;
	private final MonthUsageList monthUsageList;
	
	private RootController root;
	private List<Integer> years;

	GridPane chartControlPanel;
	ToggleGroup meterGroup;
	RadioButton radioLowElectricityPurchased;
	RadioButton radioLowElectricityDelivered;
	RadioButton radioHighElectricityPurchased;
	RadioButton radioHighElectricityDelivered;
	RadioButton radioTotalElectricity;
	RadioButton radioGasPurchased;
	RadioButton radioWaterPurchased;
	
	List<RadioButton> yearRadioButtons;

	public ChartControlPanelView(MonthUsageChartView monthUsageChartView, MonthUsageList monthUsageList) {
		this.monthUsageChartView = monthUsageChartView;
		this.monthUsageList = monthUsageList;
		years = monthUsageList.getReadOnlyYearList();
		monthUsageList.addListener(this);
		meterGroup = new ToggleGroup();
		chartControlPanel = new GridPane();
		chartControlPanel.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		chartControlPanel.setPadding(new Insets(10, 10, 10, 10));
		chartControlPanel.setHgap(5);
		chartControlPanel.setVgap(5);
        chartControlPanel.add(createMeterToggleGroupBox(), 0, 0);
        chartControlPanel.add(createYearGroupBox(), 10, 0);
//        chartControlPanel.setGridLinesVisible(true);
	}
	
	@Override
	public void onChanged(Change<? extends Integer> c) {
		years = monthUsageList.getReadOnlyYearList();
		chartControlPanel.getChildren().remove(1);
		chartControlPanel.add(createYearGroupBox(), 10, 0);
		
		List<Integer> selectedYears = new ArrayList<>();
		for (RadioButton yearButton : yearRadioButtons) {
			if (yearButton.isSelected()) {
				selectedYears.add((Integer)yearButton.getUserData());
			}
		}
		monthUsageChartView.setYears(selectedYears);
	}
	
	public GridPane getPanel(RootController root) {
		this.root = root;
		return chartControlPanel;
	}
	
	private void setMeter(ActionEvent actionEvent) {
		Toggle toggle = meterGroup.getSelectedToggle();
		monthUsageChartView.setMeter((Meters)toggle.getUserData());
		
		// Tricky way to trigger root controller to update the graphical view!
		if (root != null) {
			root.showGraphicalView(actionEvent);
		}
	}

	private void setYear(ActionEvent actionEvent) {
		List<Integer> selectedYears = new ArrayList<>();
		for (RadioButton yearButton : yearRadioButtons) {
			if (yearButton.isSelected()) {
				selectedYears.add((Integer)yearButton.getUserData());
			}
		}

		monthUsageChartView.setYears(selectedYears);
		// Tricky way to trigger root controller to update the graphical view!
		if (root != null) {
			root.showGraphicalView(actionEvent);
		}
	}

	private HBox createMeterToggleGroupBox() {
		radioLowElectricityPurchased = new RadioButton(Meters.LOW_ELECTRICITY_PURCHASED.nlName());
		radioLowElectricityDelivered = new RadioButton(Meters.LOW_ELECTRICITY_DELIVERED.nlName());
		radioHighElectricityPurchased = new RadioButton(Meters.HIGH_ELECTRICITY_PURCHASED.nlName());
		radioHighElectricityDelivered = new RadioButton(Meters.HIGH_ELECTRICITY_DELIVERED.nlName());
		radioTotalElectricity = new RadioButton(Meters.TOTAL_ELECTRICITY.nlName());
		radioGasPurchased = new RadioButton(Meters.GAS_PURCHASED.nlName());
		radioGasPurchased.setSelected(true); // Default view
		radioWaterPurchased = new RadioButton(Meters.WATER_PURCHASED.nlName());
		
		radioLowElectricityPurchased.setToggleGroup(meterGroup);
		radioLowElectricityDelivered.setToggleGroup(meterGroup);
		radioHighElectricityPurchased.setToggleGroup(meterGroup);
		radioHighElectricityDelivered.setToggleGroup(meterGroup);
		radioTotalElectricity.setToggleGroup(meterGroup);
		radioGasPurchased.setToggleGroup(meterGroup);
		radioWaterPurchased.setToggleGroup(meterGroup);
		
		radioLowElectricityPurchased.setUserData(Meters.LOW_ELECTRICITY_PURCHASED);
		radioLowElectricityDelivered.setUserData(Meters.LOW_ELECTRICITY_DELIVERED);
		radioHighElectricityPurchased.setUserData(Meters.HIGH_ELECTRICITY_PURCHASED);
		radioHighElectricityDelivered.setUserData(Meters.HIGH_ELECTRICITY_DELIVERED);
		radioTotalElectricity.setUserData(Meters.TOTAL_ELECTRICITY);
		radioGasPurchased.setUserData(Meters.GAS_PURCHASED);
		radioWaterPurchased.setUserData(Meters.WATER_PURCHASED);
		
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
				radioHighElectricityDelivered
				);
		vbox1.setSpacing(10);
		VBox vbox2 = new VBox(radioTotalElectricity,
				radioGasPurchased,
				radioWaterPurchased);
		vbox2.setSpacing(10);
		HBox hbox = new HBox(vbox1, vbox2);
		hbox.setSpacing(30);
		return hbox;
	}
	
	private HBox createYearGroupBox() {
		VBox vbox = new VBox();
		vbox.setSpacing(10);
		HBox hbox = new HBox();
		hbox.setSpacing(30);
		yearRadioButtons = new ArrayList<>();
		int columnCount = 0;
		for (Integer year : years) {
			if ((columnCount % 5) == 0) {
				if (!vbox.getChildren().isEmpty()) {
					hbox.getChildren().add(vbox);
				}
				vbox = new VBox();
				vbox.setSpacing(10);
			}
			RadioButton yearButton = new RadioButton(year.toString());
			yearButton.setUserData(year);
			yearButton.setOnAction(this::setYear);
			yearRadioButtons.add(yearButton);
			vbox.getChildren().add(yearButton);
			columnCount++;
		}
		if (!vbox.getChildren().isEmpty()) {
			hbox.getChildren().add(vbox);
		}
		return hbox;
	}

}

package nl.gwe.controllers;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import nl.gwe.domain.MeterValues;
import nl.gwe.services.MeasurementService;

@Slf4j
@Controller
@FxmlView("meterFormDialog.fxml")
public class MeterFormDialogController {

	private final MeasurementService measurementService;

	public MeterFormDialogController(MeasurementService measurementService) {
		this.measurementService = measurementService;
	}

	private Stage stage;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private DatePicker dateField;

	@FXML
	private TextField lowElectricityPurchasedField;

	@FXML
	private TextField lowElectricityDeliveredField;

	@FXML
	private TextField highElectricityPurchasedField;

	@FXML
	private TextField highElectricityDeliveredField;

	@FXML
	private TextField gasPurchasedField;

	@FXML
	private TextField waterPurchasedField;

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	@FXML
	public void initialize() {
		this.stage = new Stage();
		stage.setScene(new Scene(anchorPane));
		stage.setTitle("Meterstanden invullen");
		lowElectricityPurchasedField.setTextFormatter(new TextFormatter<>(electricityFilter));
		lowElectricityDeliveredField.setTextFormatter(new TextFormatter<>(electricityFilter));
		highElectricityPurchasedField.setTextFormatter(new TextFormatter<>(electricityFilter));
		highElectricityDeliveredField.setTextFormatter(new TextFormatter<>(electricityFilter));
		gasPurchasedField.setTextFormatter(new TextFormatter<>(gasAndWaterFilter));
		waterPurchasedField.setTextFormatter(new TextFormatter<>(gasAndWaterFilter));
		lowElectricityPurchasedField.requestFocus();
	}

	public void show() {
		dateField.setValue(LocalDate.now());
		stage.show();

	}

	@FXML
	public void okButtonAction() {
		MeterValues meterReading = new MeterValues.Builder()
				.setLowElectricityPurchased(Float.parseFloat(lowElectricityPurchasedField.getText()))
				.setHighElectricityPurchased(Float.parseFloat(highElectricityPurchasedField.getText()))
				.setGasPurchased(Float.parseFloat(gasPurchasedField.getText()))
				.setWaterPurchased(Float.parseFloat(waterPurchasedField.getText()))
				.build();
		
		// Optional fields
		if (!lowElectricityDeliveredField.getText().isBlank()) {
			meterReading.setLowElectricityDelivered(Float.parseFloat(lowElectricityDeliveredField.getText()));
		}
		if (!highElectricityDeliveredField.getText().isBlank()) {
			meterReading.setHighElectricityDelivered(Float.parseFloat(highElectricityDeliveredField.getText()));
		}
		
		measurementService.submit(dateField.getValue(), meterReading);
		stage.close();
	}

	@FXML
	public void cancelButtonAction() {
		stage.close();
	}
	
	UnaryOperator<Change> electricityFilter = change -> {
		String input = change.getControlNewText();
		if (input.matches("-?([1-9][0-9]*)?")) {
			return change;
		}
		return null;
	};
	
	UnaryOperator<Change> gasAndWaterFilter = change -> {
		String input = change.getControlNewText();
		if (input.matches("(-?[0-9]*)(\\.\\d{0,3})?")) {
			return change;
		}
		return null;
	};
}

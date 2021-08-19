package nl.gwe.controllers;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import org.springframework.stereotype.Controller;

import javafx.collections.ObservableList;
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
import nl.gwe.domain.Measurement;
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
		lowElectricityPurchasedField.setTextFormatter(new TextFormatter<>(integerFilter));
		lowElectricityDeliveredField.setTextFormatter(new TextFormatter<>(integerFilter));
		highElectricityPurchasedField.setTextFormatter(new TextFormatter<>(integerFilter));
		highElectricityDeliveredField.setTextFormatter(new TextFormatter<>(integerFilter));
		gasPurchasedField.setTextFormatter(new TextFormatter<>(integerFilter));
		waterPurchasedField.setTextFormatter(new TextFormatter<>(integerFilter));
		lowElectricityPurchasedField.requestFocus();
	}

	public void show() {
		dateField.setValue(LocalDate.now());
		stage.show();

	}

	@FXML
	public void okButtonAction() {
		MeterValues meterReading = new MeterValues.Builder()
				.setLowElectricityPurchased(Integer.parseInt(lowElectricityPurchasedField.getText()))
				.setLowElectricityDelivered(Integer.parseInt(lowElectricityDeliveredField.getText()))
				.setHighElectricityPurchased(Integer.parseInt(highElectricityPurchasedField.getText()))
				.setHighElectricityDelivered(Integer.parseInt(highElectricityDeliveredField.getText()))
				.setGasPurchased(Integer.parseInt(gasPurchasedField.getText()))
				.setWaterPurchased(Integer.parseInt(waterPurchasedField.getText()))
				.build();
		measurementService.submit(meterReading, dateField.getValue());
		stage.close();
	}

	@FXML
	public void cancelButtonAction() {
		stage.close();
	}
	
	UnaryOperator<Change> integerFilter = change -> {
		String input = change.getControlNewText();
		if (input.matches("([1-9][0-9]*)?")) {
			return change;
		}
		return null;
	};
}
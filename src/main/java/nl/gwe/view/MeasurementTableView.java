package nl.gwe.view;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.Meters;
import nl.gwe.domain.Measurement;
import nl.gwe.services.MeasurementService;

@Slf4j
@Component
public class MeasurementTableView {

	private final MeasurementService measurementService;

	private TableView<Measurement> table;

	public MeasurementTableView(MeasurementService measurementService) {

		this.measurementService = measurementService;
		table = new TableView<>();
		table.setItems(this.measurementService.getReadOnlyMeasurementList());

		TableColumn<Measurement, LocalDate> colDate = new TableColumn<>("Datum");
		TableColumn<Measurement, Float> colLowElectricityPurchased = new TableColumn<>(Meters.LOW_ELECTRICITY_PURCHASED.nlName());
		TableColumn<Measurement, Float> colLowElectricityDelivered = new TableColumn<>(Meters.LOW_ELECTRICITY_DELIVERED.nlName());
		TableColumn<Measurement, Float> colHighElectricityPurchased = new TableColumn<>(Meters.HIGH_ELECTRICITY_PURCHASED.nlName());
		TableColumn<Measurement, Float> colHighElectricityDelivered = new TableColumn<>(Meters.HIGH_ELECTRICITY_DELIVERED.nlName());
		TableColumn<Measurement, Float> colGas = new TableColumn<>(Meters.GAS_PURCHASED.nlName());
		TableColumn<Measurement, Float> colWater = new TableColumn<>(Meters.WATER_PURCHASED.nlName());

		colDate.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		colLowElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colLowElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
		colGas.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
		colWater.prefWidthProperty().bind(table.widthProperty().multiply(0.08));

		colDate.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getStartDate()));
		colLowElectricityPurchased.setCellValueFactory(
				p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getLowElectricityPurchased()));
		colLowElectricityDelivered.setCellValueFactory(
				p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getLowElectricityDelivered()));
		colHighElectricityPurchased.setCellValueFactory(
				p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getHighElectricityPurchased()));
		colHighElectricityDelivered.setCellValueFactory(
				p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getHighElectricityDelivered()));
		colGas.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getGasPurchased()));
		colWater.setCellValueFactory(
				p -> new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getWaterPurchased()));

		table.getColumns().add(colDate);
		table.getColumns().add(colLowElectricityPurchased);
		table.getColumns().add(colLowElectricityDelivered);
		table.getColumns().add(colHighElectricityPurchased);
		table.getColumns().add(colHighElectricityDelivered);
		table.getColumns().add(colGas);
		table.getColumns().add(colWater);

	}

	public TableView<Measurement> getTableView() {
		return table;
	}
}

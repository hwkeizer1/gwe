package nl.gwe.view;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import nl.gwe.domain.Measurement;
import nl.gwe.services.MeasurementService;

@Component
public class UsageTableView {

	private final MeasurementService measurementService;

	private TableView<Measurement> table;

	public UsageTableView(MeasurementService measurementService) {

		this.measurementService = measurementService;
		table = new TableView<>();
		table.setItems(this.measurementService.getReadOnlyMeasurementList());

		TableColumn<Measurement, LocalDate> colBeginDate = new TableColumn<>("Begin datum");
		TableColumn<Measurement, LocalDate> colEndDate = new TableColumn<>("Eind datum");
		TableColumn<Measurement, Float> colLowElectricityPurchased = new TableColumn<>("Elektra laag afgenomen");
		TableColumn<Measurement, Float> colLowElectricityDelivered = new TableColumn<>("Elektra laag geleverd");
		TableColumn<Measurement, Float> colHighElectricityPurchased = new TableColumn<>("Elektra hoog afgenomen");
		TableColumn<Measurement, Float> colHighElectricityDelivered = new TableColumn<>("Elektra hoog geleverd");
		TableColumn<Measurement, Float> colGas = new TableColumn<>("Gas");
		TableColumn<Measurement, Float> colWater = new TableColumn<>("Water");

		colBeginDate.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		colEndDate.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		colLowElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
		colLowElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
		colHighElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
		colHighElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
		colGas.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		colWater.prefWidthProperty().bind(table.widthProperty().multiply(0.09));

		colBeginDate.setCellValueFactory(
				new Callback<CellDataFeatures<Measurement, LocalDate>, ObservableValue<LocalDate>>() {
					public ObservableValue<LocalDate> call(CellDataFeatures<Measurement, LocalDate> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getStartDate());
					}
				});
		
		colEndDate.setCellValueFactory(
				new Callback<CellDataFeatures<Measurement, LocalDate>, ObservableValue<LocalDate>>() {
					public ObservableValue<LocalDate> call(CellDataFeatures<Measurement, LocalDate> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getEndDate());
					}
				});

		colLowElectricityPurchased
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
					public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
						if (p.getValue().getUsages() == null) return null;
						return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getLowElectricityPurchased());
					}
				});

		colLowElectricityDelivered
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
					public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
						if (p.getValue().getUsages() == null) return null;
						return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getLowElectricityDelivered());
					}
				});

		colHighElectricityPurchased
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
					public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
						if (p.getValue().getUsages() == null) return null;
						return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getHighElectricityPurchased());
					}
				});

		colHighElectricityDelivered
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
					public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
						if (p.getValue().getUsages() == null) return null;
						return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getHighElectricityDelivered());
					}
				});

		colGas.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
			public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
				if (p.getValue().getUsages() == null) return null;
				return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getGasPurchased());
			}
		});

		colWater.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Float>, ObservableValue<Float>>() {
			public ObservableValue<Float> call(CellDataFeatures<Measurement, Float> p) {
				if (p.getValue().getUsages() == null) return null;
				return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getWaterPurchased());
			}
		});

		table.getColumns().add(colBeginDate);
		table.getColumns().add(colEndDate);
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

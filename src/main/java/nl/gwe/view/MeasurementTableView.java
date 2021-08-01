package nl.gwe.view;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j
@Component
public class MeasurementTableView {

	private final MeasurementRepository measurementRepository;

	private TableView<Measurement> table;

	private ObservableList<Measurement> measurements;

	public MeasurementTableView(MeasurementRepository measurementRepository) {

		this.measurementRepository = measurementRepository;
		table = new TableView<>();

		TableColumn<Measurement, LocalDate> colDate = new TableColumn<>("Datum");
		TableColumn<Measurement, Integer> colLowElectricityPurchased = new TableColumn<>("Elektra laag afgenomen");
		TableColumn<Measurement, Integer> colLowElectricityDelivered = new TableColumn<>("Elektra laag geleverd");
		TableColumn<Measurement, Integer> colHighElectricityPurchased = new TableColumn<>("Elektra hoog afgenomen");
		TableColumn<Measurement, Integer> colHighElectricityDelivered = new TableColumn<>("Elektra hoog geleverd");
		TableColumn<Measurement, Integer> colGas = new TableColumn<>("Gas");
		TableColumn<Measurement, Integer> colWater = new TableColumn<>("Water");

		colDate.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		colLowElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colLowElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colGas.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		colWater.prefWidthProperty().bind(table.widthProperty().multiply(0.09));

		colDate.setCellValueFactory(
				new Callback<CellDataFeatures<Measurement, LocalDate>, ObservableValue<LocalDate>>() {
					public ObservableValue<LocalDate> call(CellDataFeatures<Measurement, LocalDate> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getStartDate());
					}
				});

		colLowElectricityPurchased
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getLowElectricityPurchased());
					}
				});

		colLowElectricityDelivered
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getLowElectricityDelivered());
					}
				});

		colHighElectricityPurchased
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getHighElectricityPurchased());
					}
				});

		colHighElectricityDelivered
				.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
						return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getHighElectricityDelivered());
					}
				});

		colGas.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getGasPurchased());
			}
		});

		colWater.setCellValueFactory(new Callback<CellDataFeatures<Measurement, Integer>, ObservableValue<Integer>>() {
			public ObservableValue<Integer> call(CellDataFeatures<Measurement, Integer> p) {
				return new ReadOnlyObjectWrapper<>(p.getValue().getMeterValues().getWaterPurchased());
			}
		});

		table.getColumns().add(colDate);
		table.getColumns().add(colLowElectricityPurchased);
		table.getColumns().add(colLowElectricityDelivered);
		table.getColumns().add(colHighElectricityPurchased);
		table.getColumns().add(colHighElectricityDelivered);
		table.getColumns().add(colGas);
		table.getColumns().add(colWater);
		
		removeScrollBar(table);

	}

	public TableView<Measurement> getTableView() {
		measurements = FXCollections.observableList(this.measurementRepository.findAll());
		table.setItems(measurements);
		table.refresh();
		return table;
	}
	
	public <T extends Control> void removeScrollBar(T table) {
        ScrollBar scrollBar = (ScrollBar) table.queryAccessibleAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR);
        if (scrollBar != null) {
            scrollBar.setPrefHeight(0);
            scrollBar.setMaxHeight(0);
            scrollBar.setOpacity(1);
            scrollBar.setVisible(false); 
        }
    }
}

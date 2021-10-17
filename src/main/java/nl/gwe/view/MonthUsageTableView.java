package nl.gwe.view;


import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import nl.gwe.domain.MonthUsage;
import nl.gwe.services.MonthUsageService;

@Component
public class MonthUsageTableView {

	private final MonthUsageService monthUsageService;

	private TableView<MonthUsage> table;
	
	private DecimalFormat df = new DecimalFormat("0.0");

	public MonthUsageTableView(MonthUsageService monthUsageService) {

		this.monthUsageService = monthUsageService;
		table = new TableView<>();
		table.setItems(this.monthUsageService.getReadOnlyMonthUsageList());

		TableColumn<MonthUsage, String> colMonth = new TableColumn<>("Maand");
		TableColumn<MonthUsage, Float> colLowElectricityPurchased = new TableColumn<>("Elektra laag afgenomen");
		TableColumn<MonthUsage, Float> colLowElectricityDelivered = new TableColumn<>("Elektra laag geleverd");
		TableColumn<MonthUsage, Float> colHighElectricityPurchased = new TableColumn<>("Elektra hoog afgenomen");
		TableColumn<MonthUsage, Float> colHighElectricityDelivered = new TableColumn<>("Elektra hoog geleverd");
		TableColumn<MonthUsage, Float> colGas = new TableColumn<>("Gas");
		TableColumn<MonthUsage, Float> colWater = new TableColumn<>("Water");

		colMonth.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
		colLowElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colLowElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityPurchased.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colHighElectricityDelivered.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
		colGas.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		colWater.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
		
		colMonth.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getYearMonthLabel()));
		
		colLowElectricityPurchased.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getLowElectricityPurchased());
		});
		
		colLowElectricityPurchased.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		colLowElectricityDelivered.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getLowElectricityDelivered());
		});
		
		colLowElectricityDelivered.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		colHighElectricityPurchased.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getHighElectricityPurchased());
		});
		
		colHighElectricityPurchased.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		colHighElectricityDelivered.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getHighElectricityDelivered());
		});
		
		colHighElectricityDelivered.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		colGas.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getGasPurchased());
		});
		
		colGas.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		colWater.setCellValueFactory(p -> {
			if (p.getValue().getUsages() == null)
				return null;
			return new ReadOnlyObjectWrapper<>(p.getValue().getUsages().getWaterPurchased());
		});
		
		colWater.setCellFactory(tc -> new TableCell<MonthUsage, Float>() {

		    @Override
		    protected void updateItem(Float price, boolean empty) {
		        super.updateItem(price, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            setText(df.format(price));
		        }
		    }
		});

		table.getColumns().add(colMonth);
		table.getColumns().add(colLowElectricityPurchased);
		table.getColumns().add(colLowElectricityDelivered);
		table.getColumns().add(colHighElectricityPurchased);
		table.getColumns().add(colHighElectricityDelivered);
		table.getColumns().add(colGas);
		table.getColumns().add(colWater);

	}

	public TableView<MonthUsage> getTableView() {
		return table;
	}
}

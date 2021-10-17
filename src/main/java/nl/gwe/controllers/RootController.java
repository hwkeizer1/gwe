package nl.gwe.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import nl.gwe.view.MeasurementTableView;
import nl.gwe.view.UsageChartView;
import nl.gwe.view.MeasurementUsageTableView;

@Controller
@FxmlView("root.fxml")
public class RootController implements Initializable {
	
	private final FxWeaver fxWeaver;
	private final MeasurementTableView measurementTableView;
	private final MeasurementUsageTableView usageTableView;
	private final UsageChartView usageChartView;
	
	@FXML
	private BorderPane rootWindow;
	
	public RootController(FxWeaver fxWeaver, 
			MeasurementTableView measurementTableView, 
			MeasurementUsageTableView usageTableView,
			UsageChartView usageChartView) {
		this.fxWeaver = fxWeaver;
		this.measurementTableView = measurementTableView;
		this.usageTableView = usageTableView;
		this.usageChartView = usageChartView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@FXML
	public void showMeterFormDialog(ActionEvent actionEvent) {
		fxWeaver.loadController(MeterFormDialogController.class).show();
	}
	
	@FXML
	public void showExportPeriodDialog(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void showCreateBackupDialog(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void showRestoreBackupDialog(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void closeProgram(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void showGraphicalView(ActionEvent actionEvent) {
		rootWindow.setCenter(usageChartView.initUsageChart());
	}
	
	@FXML
	public void showMeasurementTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(measurementTableView.getTableView());
	}
	
	@FXML
	public void showUsageTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(usageTableView.getTableView());
	}

}
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
import nl.gwe.view.ChartControlPanelView;
import nl.gwe.view.MeasurementTableView;
import nl.gwe.view.MonthUsageChartView;
import nl.gwe.view.MonthUsageTableView;

@Controller
@FxmlView("root.fxml")
public class RootController implements Initializable {
	
	private final FxWeaver fxWeaver;
	private final MeasurementTableView measurementTableView;
	private final MonthUsageTableView monthUsageTableView;
	private final MonthUsageChartView monthUsageChartView;
	private final ChartControlPanelView chartControlPanel;
	
	
	@FXML
	private BorderPane rootWindow;
	
	public RootController(FxWeaver fxWeaver, 
			MeasurementTableView measurementTableView, 
			MonthUsageChartView monthUsageChartView,
			MonthUsageTableView monthUsageTableView,
			ChartControlPanelView chartControlPanel) {
		this.fxWeaver = fxWeaver;
		this.measurementTableView = measurementTableView;
		this.monthUsageChartView = monthUsageChartView;
		this.monthUsageTableView = monthUsageTableView;
		this.chartControlPanel = chartControlPanel;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
		rootWindow.setCenter(monthUsageChartView.getMonthUsageChartView());
		rootWindow.setBottom(chartControlPanel.getPanel(this));
	}
	
	@FXML
	public void showMeasurementTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(measurementTableView.getTableView());
	}
	
	@FXML
	public void showMonthUsageTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(monthUsageTableView.getTableView());
	}

}
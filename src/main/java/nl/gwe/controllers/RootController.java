package nl.gwe.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import nl.gwe.domain.Measurement;
import nl.gwe.services.MeasurementService;
import nl.gwe.view.ChartControlPanelView;
import nl.gwe.view.MeasurementTableView;
import nl.gwe.view.MonthUsageChartView;
import nl.gwe.view.MonthUsageTableView;
import nl.gwe.view.YearUsageChartView;

@Slf4j
@Controller
@FxmlView("root.fxml")
public class RootController implements Initializable {
	
	private final FxWeaver fxWeaver;
	private final MeasurementTableView measurementTableView;
	private final MonthUsageTableView monthUsageTableView;
	private final MonthUsageChartView monthUsageChartView;
	private final YearUsageChartView yearUsageChartView;
	private final ChartControlPanelView chartControlPanel;
	
	private final MeasurementService measurementService;
	
	
	@FXML
	private BorderPane rootWindow;
	
	public RootController(FxWeaver fxWeaver, 
			MeasurementTableView measurementTableView, 
			MonthUsageChartView monthUsageChartView,
			YearUsageChartView yearUsageChartView,
			MonthUsageTableView monthUsageTableView,
			ChartControlPanelView chartControlPanel,
			MeasurementService measurementService) {
		this.fxWeaver = fxWeaver;
		this.measurementTableView = measurementTableView;
		this.monthUsageChartView = monthUsageChartView;
		this.yearUsageChartView = yearUsageChartView;
		this.monthUsageTableView = monthUsageTableView;
		this.chartControlPanel = chartControlPanel;
		this.measurementService = measurementService;
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
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GWE Backup files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        LocalDate date = LocalDate.now();
        fileChooser.setInitialFileName("GWE-Backup_" + date + ".txt");
        
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
        	List<Measurement> measurements = measurementService.getReadOnlyMeasurementList();
        	try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				String backup = objectMapper.writeValueAsString(measurements);
				saveTextToFile(backup, file);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				log.error("Fout bij het maken van de backup");
			}
            
        }
	}
	
	private void saveTextToFile(String content, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(content);
        } catch (IOException ex) {
            log.error("Fout bij het schrijven van de backup file");
        }
    }
	
	@FXML
	public void showRestoreBackupDialog(ActionEvent actionEvent) {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GWE Backup files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
        		String backup = readTextFromFile(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				try {
					List<Measurement> measurements = objectMapper.readValue(backup, new TypeReference<List<Measurement>>() {});
					for (Measurement measurement: measurements) {
						measurement.getMeterValues().setId(null);
						measurementService.submit(measurement.getStartDate(), measurement.getMeterValues());
					}
				} catch (JsonProcessingException e) {
					log.error("Fout bij het terugzetten van de backup");
				}
            
        }
	}
	
	private String readTextFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.readLine();
        } catch (IOException e) {
			log.error("Fout bij het lezen van de backup file");
		}
		return null; 
    }
	
	@FXML
	public void closeProgram(ActionEvent actionEvent) {
		
	}
	
	@FXML
	public void showMonthGraphicalView(ActionEvent actionEvent) {
		rootWindow.setCenter(monthUsageChartView.getMonthUsageChartView());
		rootWindow.setBottom(chartControlPanel.getPanel(this, "month"));
	}
	
	@FXML
	public void showYearGraphicalView(ActionEvent actionEvent) {
		rootWindow.setCenter(yearUsageChartView.getYearUsageChartView());
		rootWindow.setBottom(chartControlPanel.getPanel(this, "year"));
	}
	
	@FXML
	public void showMeasurementTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(measurementTableView.getTableView());
		rootWindow.setBottom(null);
	}
	
	@FXML
	public void showMonthUsageTableView(ActionEvent actionEvent) {
		rootWindow.setCenter(monthUsageTableView.getTableView());
		rootWindow.setBottom(null);
	}

}
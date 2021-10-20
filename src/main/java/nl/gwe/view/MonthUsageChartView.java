package nl.gwe.view;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import nl.gwe.converters.IntegerToMonthConverter;
import nl.gwe.domain.MonthUsage;
import nl.gwe.services.MonthUsageService;

@Component
public class MonthUsageChartView {
	
	MonthUsageService monthUsageService;
	
	IntegerToMonthConverter c = new IntegerToMonthConverter();
	
	public LineChart<Integer, Integer> initUsageChart() {
		NumberAxis xAxis = new NumberAxis(0, 13, 1);
		NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Maanden");
		xAxis.setTickLabelFormatter(c);
		yAxis.setLabel("Gas verbruik");
		
		LineChart<Integer, Integer> lineChart = new LineChart(xAxis, yAxis);
		lineChart.setTitle("Gasverbruik");
		
		var<Integer, Integer> data2021 = new XYChart.Series<Integer, Integer>();
		data2021.setName("2021");
		
		data2021.getData().add(new XYChart.Data<>(1, 16));
        data2021.getData().add(new XYChart.Data<>(2, 14));
        data2021.getData().add(new XYChart.Data<>(3, 15));
        data2021.getData().add(new XYChart.Data<>(4, 12));
        data2021.getData().add(new XYChart.Data<>(5, 7));
        data2021.getData().add(new XYChart.Data<>(6, 9));
        
        var<Integer, Integer> data2020 = new XYChart.Series<Integer, Integer>();
		data2020.setName("2020");
		
		data2020.getData().add(new XYChart.Data<>(1, 25));
        data2020.getData().add(new XYChart.Data<>(2, 24));
        data2020.getData().add(new XYChart.Data<>(3, 15));
        data2020.getData().add(new XYChart.Data<>(4, 11));
        data2020.getData().add(new XYChart.Data<>(5, 3));
        data2020.getData().add(new XYChart.Data<>(6, 14));
        
        lineChart.getData().add(data2020);
        lineChart.getData().add(data2021);
        
        return lineChart;
	}
	
	private XYChart.Series<Integer, Integer> getDataForYear(Integer year) {
//		var<Integer, Integer> dataSet = new XYChart.Series<Integer, Integer>();
//		dataSet.setName(year.toString());
//		List<MonthUsage> monthUsages = monthUsageService.getMonthUsagesOfYear(year);
//		for (MonthUsage monthUsage : monthUsages) {
//			if (monthUsage.getDate().getYear() == year-1) {
//				dataSet.getData().add(new XYChart.Data<>(0, ));
//			}
//			dataSet.getData().add(new XYChart.Data<>());
//		}
		
		return null;
	}
	
}

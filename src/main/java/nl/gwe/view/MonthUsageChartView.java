package nl.gwe.view;

import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.converters.IntegerToMonthConverter;
import nl.gwe.datalists.ChartData;
import nl.gwe.datalists.Meters;
import nl.gwe.datalists.MonthUsageChartData;
import nl.gwe.domain.MonthUsage;
import nl.gwe.services.MonthUsageService;

@Slf4j
@Component
public class MonthUsageChartView {
	
	private final MonthUsageService monthUsageService;
	
	private final MonthUsageChartData monthUsageChartData;
	
	IntegerToMonthConverter c = new IntegerToMonthConverter();
	
	public MonthUsageChartView(MonthUsageService monthUsageService, MonthUsageChartData monthUsageChartData) {
		this.monthUsageService = monthUsageService;
		this.monthUsageChartData = monthUsageChartData;

	}

	public LineChart<Integer, Integer> initUsageChart() {
		NumberAxis xAxis = new NumberAxis(0, 13, 1);
		NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Maanden");
		xAxis.setTickLabelFormatter(c);
		yAxis.setLabel("Gas verbruik");
		
		LineChart<Integer, Integer> lineChart = new LineChart(xAxis, yAxis);
		lineChart.setTitle("Gasverbruik");
	
        var<Integer, Integer> data2019 = new XYChart.Series<Integer, Integer>();
        data2019.setName("2019");
		
        data2019.getData().add(new XYChart.Data<>(0, 19));
        data2019.getData().add(new XYChart.Data<>(1, 25));
        data2019.getData().add(new XYChart.Data<>(2, 21));
        data2019.getData().add(new XYChart.Data<>(3, 15));
        data2019.getData().add(new XYChart.Data<>(4, 11));
        data2019.getData().add(new XYChart.Data<>(5, 3));
        data2019.getData().add(new XYChart.Data<>(6, 3));
        data2019.getData().add(new XYChart.Data<>(7, 6));
        data2019.getData().add(new XYChart.Data<>(8, 5));
        data2019.getData().add(new XYChart.Data<>(9, 5));
        data2019.getData().add(new XYChart.Data<>(10, 6));
        data2019.getData().add(new XYChart.Data<>(11, 7));
        data2019.getData().add(new XYChart.Data<>(12, 11));
        data2019.getData().add(new XYChart.Data<>(13, 15));
        
        lineChart.getData().add(data2019);
        
        Integer year = 2020;
		var<Integer, Integer> dataSet = new XYChart.Series<Integer, Integer>();
		dataSet.setName(year.toString());
		
		// Test part
		dataSet = getDataForYear(year);
		
//        lineChart.getData().add(dataSet);
        
        return lineChart;
	}
	
	private XYChart.Series<Integer, Integer> getDataForYear(Integer year) {
		EnumMap<Meters, List<ChartData>> meters = monthUsageChartData.getChartDataForYear(year);
		log.debug("{}", meters);
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

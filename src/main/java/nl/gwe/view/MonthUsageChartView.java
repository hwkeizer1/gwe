package nl.gwe.view;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.converters.IntegerToMonthConverter;
import nl.gwe.datalists.ChartData;
import nl.gwe.datalists.Meters;
import nl.gwe.datalists.MonthUsageChartData;
import nl.gwe.services.MonthUsageService;

@Slf4j
@Component
public class MonthUsageChartView {

	private Meters meter = Meters.GAS_PURCHASED;

	private List<Integer> years;

	LineChart<Number, Number> lineChart;
	NumberAxis xAxis;
	NumberAxis yAxis;

	private final MonthUsageService monthUsageService;

	private final MonthUsageChartData monthUsageChartData;

	IntegerToMonthConverter c = new IntegerToMonthConverter();

	public MonthUsageChartView(MonthUsageService monthUsageService, MonthUsageChartData monthUsageChartData) {
		this.monthUsageService = monthUsageService;
		this.monthUsageChartData = monthUsageChartData;

		years = new ArrayList<>();

		xAxis = new NumberAxis(0, 13, 1);
		xAxis.setLabel("Maanden");
		xAxis.setTickLabelFormatter(c);

		yAxis = new NumberAxis();

		// For now one test year
		years.add(2020);
		years.add(2021);

	}

	public void setMeter(Meters meter) {
		this.meter = meter;
		createChartData();
	}

	public void addYear(Integer year) {
		years.add(year);
		updateChartData();
	}

	public void removeYear(Integer year) {
		if (years.remove(year)) {
			updateChartData();
		}
	}
	
	public LineChart<Number, Number> getMonthUsageChartView() {
		createChartData();
		return lineChart;
	}

	public void createChartData() {
		lineChart = new LineChart<>(xAxis, yAxis);
		yAxis.setLabel(meter.toString());
		lineChart.setTitle(meter.toString());
		
		for (Integer year : years) {
			var<Integer, Integer> series = getYearSeries(year);
			lineChart.getData().add(series);
		}
	}
	
	private XYChart.Series<Number, Number> getYearSeries(Integer year) {
		EnumMap<Meters, List<ChartData>> meters = monthUsageChartData.getChartDataForYear(year);
		XYChart.Series<Number, Number> series = new XYChart.Series<>(); 
		series.setName(year.toString());
		List<ChartData> chartData = meters.get(meter);
		for (ChartData data : chartData) {
			log.debug("meter {}:{}", data.getMonth(), Math.round(data.getValue()));
			series.getData().add(new XYChart.Data(data.getMonth(), Math.round(data.getValue())));
			log.debug("dataset {}", series);
		}

		return series;
	}
	
	public void updateChartData() {
//		log.debug("In update chart");
//		lineChart = new LineChart<>(xAxis, yAxis);
//		for (Integer year : years) {
//			var<Integer, Integer> series = getDataForYear(year);
//			lineChart.getData().add(series);
//		}
	}
	
	

//	public LineChart<Integer, Integer> initUsageChart() {
//
//		var<Integer, Integer> data2019 = new XYChart.Series<Integer, Integer>();
//		data2019.setName("2019");
//
//		data2019.getData().add(new XYChart.Data<>(0, 19));
//		data2019.getData().add(new XYChart.Data<>(1, 25));
//		data2019.getData().add(new XYChart.Data<>(2, 21));
//		data2019.getData().add(new XYChart.Data<>(3, 15));
//		data2019.getData().add(new XYChart.Data<>(4, 11));
//		data2019.getData().add(new XYChart.Data<>(5, 3));
//		data2019.getData().add(new XYChart.Data<>(6, 3));
//		data2019.getData().add(new XYChart.Data<>(7, 6));
//		data2019.getData().add(new XYChart.Data<>(8, 5));
//		data2019.getData().add(new XYChart.Data<>(9, 5));
//		data2019.getData().add(new XYChart.Data<>(10, 6));
//		data2019.getData().add(new XYChart.Data<>(11, 7));
//		data2019.getData().add(new XYChart.Data<>(12, 11));
//		data2019.getData().add(new XYChart.Data<>(13, 15));
//
//		lineChart.getData().add(data2019);
//
//		Integer year = 2020;
//		var<Integer, Integer> dataSet = new XYChart.Series<Integer, Integer>();
//		dataSet.setName(year.toString());
//
//		// Test part
//		dataSet = getDataForYear(year);
//
////        lineChart.getData().add(dataSet);
//
//		return lineChart;
//	}
}

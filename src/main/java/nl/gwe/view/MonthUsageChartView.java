package nl.gwe.view;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.converters.IntegerToMonthConverter;
import nl.gwe.datalists.MonthChartData;
import nl.gwe.datalists.Meters;
import nl.gwe.datalists.MonthUsageChartData;

@Slf4j
@Component
public class MonthUsageChartView {

	private Meters meter = Meters.GAS_PURCHASED;

	private List<Integer> selectedYears;

	LineChart<Number, Number> lineChart;
	NumberAxis xAxis;
	NumberAxis yAxis;

	private final MonthUsageChartData monthUsageChartData;

	IntegerToMonthConverter c = new IntegerToMonthConverter();

	public MonthUsageChartView(MonthUsageChartData monthUsageChartData) {
		this.monthUsageChartData = monthUsageChartData;

		selectedYears = new ArrayList<>();

		xAxis = new NumberAxis(0, 13, 1);
		xAxis.setLabel("Maanden");
		xAxis.setTickLabelFormatter(c);

		yAxis = new NumberAxis();
	}

	public void setMeter(Meters meter) {
		this.meter = meter;
		createChartData();
	}

	public void setYears(List<Integer> years) {
		selectedYears = years;
		createChartData();
	}

	
	public LineChart<Number, Number> getMonthUsageChartView() {
		createChartData();
		return lineChart;
	}

	public void createChartData() {
		lineChart = new LineChart<>(xAxis, yAxis);
		yAxis.setLabel(meter.nlName() + " " + meter.unit());
		lineChart.setTitle(meter.nlName());
		
		for (Integer year : selectedYears) {
			XYChart.Series<Number, Number> series = getYearSeries(year);
			lineChart.getData().add(series);
		}
	}
	
	private XYChart.Series<Number, Number> getYearSeries(Integer year) {
		EnumMap<Meters, List<MonthChartData>> meters = monthUsageChartData.getMonthUsageChartDataForYear(year);
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		series.setName(year.toString());
		List<MonthChartData> chartData = meters.get(meter);
		for (MonthChartData data : chartData) {
			series.getData().add(new XYChart.Data<>(data.getMonth(), data.getValue()));
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
}

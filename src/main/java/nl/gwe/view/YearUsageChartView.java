package nl.gwe.view;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.YearChartData;
import nl.gwe.datalists.Meters;
import nl.gwe.datalists.YearUsageChartData;

@Slf4j
@Component
public class YearUsageChartView {

	private Meters meter = Meters.GAS_PURCHASED;

	private List<Integer> selectedYears;

	BarChart<String, Number> barChart;
	CategoryAxis xAxis;
	NumberAxis yAxis;

	private final YearUsageChartData yearUsageChartData;

//	IntegerToMonthConverter c = new IntegerToMonthConverter();

	public YearUsageChartView(YearUsageChartData yearUsageChartData) {
		this.yearUsageChartData = yearUsageChartData;

		selectedYears = new ArrayList<>();

		xAxis = new CategoryAxis();
		xAxis.setLabel("Jaren");
//		xAxis.setTickLabelFormatter(c);

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

	
	public BarChart<String, Number> getYearUsageChartView() {
		createChartData();
		return barChart;
	}

	public void createChartData() {
		barChart = new BarChart<>(xAxis, yAxis);
		yAxis.setLabel(meter.nlName() + " " + meter.unit());
		barChart.setTitle(meter.nlName());
		
		Series<String, Number> serie = getYearOverview();
		barChart.getData().add(serie);
	}
	
	private XYChart.Series<String, Number> getYearOverview() {
		EnumMap<Meters, List<YearChartData>> meters = yearUsageChartData.getYearUsageChartData();
		XYChart.Series<String, Number> serie = new XYChart.Series<>();
		serie.setName("Jaar overzicht");
		List<YearChartData> chartData = meters.get(meter);
		for (YearChartData data : chartData) {
			serie.getData().add(new XYChart.Data<>(data.getYear().toString(), data.getValue()));
		}
		return serie;
	}
}

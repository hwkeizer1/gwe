package nl.gwe.view;

import org.springframework.stereotype.Component;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

@Component
public class UsageChartView {
	
	public LineChart initUsageChart() {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Maanden");
		yAxis.setLabel("Gas verbruik");
		
		LineChart lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle("Gasverbruik");
		
		var data2021 = new XYChart.Series<Integer, Integer>();
		data2021.setName("2021");
		
		data2021.getData().add(new XYChart.Data<>(0, 16));
        data2021.getData().add(new XYChart.Data<>(30, 14));
        data2021.getData().add(new XYChart.Data<>(58, 15));
        data2021.getData().add(new XYChart.Data<>(88, 12));
        data2021.getData().add(new XYChart.Data<>(119, 7));
        data2021.getData().add(new XYChart.Data<>(149, 9));
        
        var data2020 = new XYChart.Series<Integer, Integer>();
		data2020.setName("2021");
		
		data2020.getData().add(new XYChart.Data<>(0, 25));
        data2020.getData().add(new XYChart.Data<>(30, 24));
        data2020.getData().add(new XYChart.Data<>(58, 15));
        data2020.getData().add(new XYChart.Data<>(88, 11));
        data2020.getData().add(new XYChart.Data<>(119, 3));
        data2020.getData().add(new XYChart.Data<>(149, 14));
        
        lineChart.getData().add(data2020);
        lineChart.getData().add(data2021);
        
        return lineChart;
	}
	
}

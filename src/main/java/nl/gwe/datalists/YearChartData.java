package nl.gwe.datalists;

import lombok.Data;

@Data
public class YearChartData {

	private Integer year;
	private Float value;
	
	public YearChartData(Integer year, Float value) {
		this.year = year;
		this.value = value;
	}
	
	
}

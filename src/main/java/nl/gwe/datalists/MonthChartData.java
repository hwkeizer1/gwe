package nl.gwe.datalists;

public class MonthChartData {

	private Integer month;
	private Float value;
	
	
	public MonthChartData(Integer month, Float value) {
		if (month >= 0 && month <= 13) {
			this.month = month;
			this.value = value;
		}
		
	}

	public Integer getMonth() {
		return month;
	}
	
	public Float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "[month=" + month + ", value=" + value + "]";
	}
	
	
}

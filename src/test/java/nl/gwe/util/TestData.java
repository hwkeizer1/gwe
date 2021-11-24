package nl.gwe.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
import nl.gwe.domain.MonthUsage;

@Slf4j
public class TestData {
	
	LocalDate refDate = LocalDate.of(2021, 9, 15);
	
	public LocalDate getRefDate() {
		return refDate;
	}
	
	public Integer getRefYear() {
		return getRefDate().getYear();
	}
	
	public ObservableList<MonthUsage> getOrderedMonthUsageListComplete() {
		List<MonthUsage> monthUsages = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			monthUsages.add(getMonthUsage(i + 0L, YearMonth.of(refDate.getYear(), i), 100.1F + i));
		}
		return FXCollections.observableList(monthUsages);
	}
	
	public ObservableList<MonthUsage> getOrderedMonthUsageListMisingFirstMonth() {
		ObservableList<MonthUsage> monthUsages = getOrderedMonthUsageListComplete();
		monthUsages.remove(0);
		return monthUsages;
	}
	
	public ObservableList<MonthUsage> getOrderedMonthUsageListMisingLastMonth() {
		ObservableList<MonthUsage> monthUsages = getOrderedMonthUsageListComplete();
		monthUsages.remove(11);
		return monthUsages;
	}
	
	public ObservableList<Measurement> getOrderedMeasurementList() {
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(getMeasurement(1L, refDate.minusDays(100), refDate.minusDays(80), 10F)); //2021-06-07
		measurements.add(getMeasurement(2L, refDate.minusDays(80), refDate.minusDays(60), 20F)); //2021-06-27
		measurements.add(getMeasurement(3L, refDate.minusDays(60), refDate.minusDays(40), 30F));	//2021-07-17
		measurements.add(getMeasurement(4L, refDate.minusDays(40), null, 40F)); //2021-08-06
		return FXCollections.observableList(measurements);
	}
	
	public MonthUsage getMonthUsage(Long id, YearMonth yearMonth, Float values) {
		MonthUsage monthUsage = new MonthUsage();
		monthUsage.setId(id);
		monthUsage.setDate(yearMonth);
		monthUsage.setUsages(getMeterValues(id + 1000, values));
		return monthUsage;
	}
	
	public Measurement getMeasurement(Long id, LocalDate startDate, LocalDate endDate, Float values) {
		Measurement measurement = new Measurement();
		measurement.setId(id);
		measurement.setStartDate(startDate);
		measurement.setEndDate(endDate);
		measurement.setMeterValues(getMeterValues(id, values));
		measurement.setUsages(getMeterValues(id + 1000, values));
		return measurement;
	}
	
	public MeterValues getMeterValues(Long id, Float value) {
		MeterValues meterValue =  new MeterValues.Builder()
				.setHighElectricityDelivered(value)
				.setHighElectricityPurchased(value)
				.setLowElectricityDelivered(value)
				.setLowElectricityPurchased(value)
				.setGasPurchased(value)
				.setWaterPurchased(value)
				.build();
		meterValue.setId(id);
		return meterValue;
	}
}

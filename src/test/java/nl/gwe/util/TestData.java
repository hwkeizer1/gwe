package nl.gwe.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;

public class TestData {
	
	LocalDate refDate = LocalDate.of(2021, 9, 15);
	
	public LocalDate getRefDate() {
		return refDate;
	}
	
	public ObservableList<Measurement> getOrderedMeasurementList() {
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(getMeasurement(1L, refDate.minusDays(100), refDate.minusDays(80), 10));
		measurements.add(getMeasurement(2L, refDate.minusDays(80), refDate.minusDays(60), 20));
		measurements.add(getMeasurement(3L, refDate.minusDays(60), refDate.minusDays(40), 30));
		measurements.add(getMeasurement(4L, refDate.minusDays(40), null, 40));
		return FXCollections.observableList(measurements);
	}
	
	public Measurement getMeasurement(Long id, LocalDate startDate, LocalDate endDate, int values) {
		Measurement measurement = new Measurement();
		measurement.setId(id);
		measurement.setStartDate(startDate);
		measurement.setEndDate(endDate);
		measurement.setMeterValues(getMeterValues(id, values));
		return measurement;
	}
	
	public MeterValues getMeterValues(Long id, int value) {
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
package nl.gwe.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;

@Component
public class Prefill {
	
	public ObservableList<Measurement> getOrderedMeasurementList() {
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(getMeasurement(1L, LocalDate.now().minusDays(100), LocalDate.now().minusDays(80), 10));
		measurements.add(getMeasurement(2L, LocalDate.now().minusDays(80), LocalDate.now().minusDays(60), 20));
		measurements.add(getMeasurement(3L, LocalDate.now().minusDays(60), LocalDate.now().minusDays(40), 30));
		measurements.add(getMeasurement(4L, LocalDate.now().minusDays(40), LocalDate.now().minusDays(20), 40));
		ObservableList<Measurement> observableMeasurements = FXCollections.observableList(measurements);
		return FXCollections.unmodifiableObservableList(observableMeasurements);
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

package nl.gwe.datalists;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j
@ExtendWith(SpringExtension.class)
class MeasurementListTest {
	
	@Mock
	MeasurementRepository measurementRepository;
	
	@InjectMocks
	MeasurementList measurementList;
	
	LocalDate refDate = LocalDate.of(2021, 9, 15);

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		when(measurementRepository.findAll()).thenReturn(getOrderedMeasurementList());
		measurementList.setObservableMeasurementList(getOrderedMeasurementList());
		log.debug("MeasurementList:\n {}", measurementList.getReadOnlyMeasurementList());
	}
	
	@Test
	void testGetLastMeasurement() {
		Optional<Measurement> expected = Optional.of(getMeasurement(4L, refDate.minusDays(40), null, 40));
		assertEquals(expected, measurementList.getLastMeasurement());
	}

	@Test
	void testGetLastMeasurementDate() {
		LocalDate expectedDate = refDate.minusDays(40);
		assertEquals(expectedDate, measurementList.getLastMeasurementDate());
	}

	@Test
	void testGetAllMeasurementsForLastMonthCalculation() {
		LocalDate lastMonthUsage = LocalDate.of(2021, 6, 1);
		List<Measurement> measurements = measurementList.getAllMeasurementsForLastMonthCalculation(lastMonthUsage);
		log.debug("AllMeasurementsForLastMonthCalculation:\n {}", measurements);
	}
	
	private ObservableList<Measurement> getOrderedMeasurementList() {
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(getMeasurement(1L, refDate.minusDays(100), refDate.minusDays(80), 10));
		measurements.add(getMeasurement(2L, refDate.minusDays(80), refDate.minusDays(60), 20));
		measurements.add(getMeasurement(3L, refDate.minusDays(60), refDate.minusDays(40), 30));
		measurements.add(getMeasurement(4L, refDate.minusDays(40), null, 40));
		return FXCollections.observableList(measurements);
	}
	
	private Measurement getMeasurement(Long id, LocalDate startDate, LocalDate endDate, int values) {
		Measurement measurement = new Measurement();
		measurement.setId(id);
		measurement.setStartDate(startDate);
		measurement.setEndDate(endDate);
		measurement.setMeterValues(getMeterValues(id, values));
		return measurement;
	}
	
	private MeterValues getMeterValues(Long id, int value) {
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

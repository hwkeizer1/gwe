package nl.gwe.datalists;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
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
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.repositories.MeasurementRepository;
import nl.gwe.util.TestData;

@Slf4j
class MeasurementListTest {
	
	@Mock
	MeasurementRepository measurementRepository;
	
	@InjectMocks
	MeasurementList measurementList;
	
	TestData testData;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		testData = new TestData();
		measurementList.setObservableMeasurementList(testData.getOrderedMeasurementList());
	}
	
	@Test
	void testGetReadOnlyMeasurementList() {
		List<Measurement> expectedList = testData.getOrderedMeasurementList();
		assertEquals(expectedList, measurementList.getReadOnlyMeasurementList());
	}
	
	@Test
	void testGetLastMeasurement_HappyPath() {
		Optional<Measurement> expected = Optional.of(testData.getMeasurement(4L, testData.getRefDate().minusDays(40), null, 40));
		assertEquals(expected, measurementList.getLastMeasurement());
	}
	
	@Test
	void testGetLastMeasurement_NotPresent() {
		measurementList.setObservableMeasurementList(FXCollections.observableList(Collections.emptyList()));
		Optional<Measurement> expected = Optional.empty();
		assertEquals(expected, measurementList.getLastMeasurement());
	}

	@Test
	void testGetLastMeasurementDate_HappyPath() {
		Optional<LocalDate> expectedDate = Optional.of(testData.getRefDate().minusDays(40));
		assertEquals(expectedDate, measurementList.getLastMeasurementDate());
	}
	
	@Test
	void testGetLastMeasurementDate_NotPresent() {
		measurementList.setObservableMeasurementList(FXCollections.observableList(Collections.emptyList()));
		assertEquals(Optional.empty(), measurementList.getLastMeasurementDate());
	}

//	@Test
//	void testGetAllMeasurementsForLastMonthCalculation() {
//		List<Measurement> expectedList = testData.getOrderedMeasurementList();
//		expectedList.remove(0);
//		
//		LocalDate lastMonthUsage = LocalDate.of(2021, 6, 1);
//		List<Measurement> measurements = measurementList.getAllMeasurementsForLastMonthCalculation(lastMonthUsage);
//		assertEquals(expectedList, measurements);
//	}
}

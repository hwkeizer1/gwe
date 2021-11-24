package nl.gwe.datalists;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	void testGetFirstMeasurement_HappyPath() {
		Optional<Measurement> expected = Optional.of(testData.getMeasurement(1L, testData.getRefDate().minusDays(100),
				testData.getRefDate().minusDays(80), 10F));
		assertEquals(expected, measurementList.getFirstMeasurement());
	}

	@Test
	void testGetFirstMeasurement_NotPresent() {
		measurementList.setObservableMeasurementList(FXCollections.observableList(Collections.emptyList()));
		Optional<Measurement> expected = Optional.empty();
		assertEquals(expected, measurementList.getFirstMeasurement());
	}

	@Test
	void testGetFirstMeasurementDate_HappyPath() {
		Optional<LocalDate> expected = Optional.of(testData.getRefDate().minusDays(100));
		assertEquals(expected, measurementList.getFirstMeasurementDate());
	}

	@Test
	void testGetFirstMeasurementDate_NotPresent() {
		measurementList.setObservableMeasurementList(FXCollections.observableList(Collections.emptyList()));
		Optional<LocalDate> expected = Optional.empty();
		assertEquals(expected, measurementList.getFirstMeasurementDate());
	}

	@Test
	void testGetLastMeasurement_HappyPath() {
		Optional<Measurement> expected = Optional
				.of(testData.getMeasurement(4L, testData.getRefDate().minusDays(40), null, 40F));
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

	@Test
	void testGetFirstMeasurementOfMonth() {
		Optional<Measurement> expected = Optional.empty();
		assertEquals(expected, measurementList.getFirstMeasurementOfMonth(YearMonth.of(2021, 5)));

		expected = Optional.of(testData.getMeasurement(1L, testData.getRefDate().minusDays(100),
				testData.getRefDate().minusDays(80), 10F));
		assertEquals(expected, measurementList.getFirstMeasurementOfMonth(YearMonth.of(2021, 6)));

		expected = Optional.of(testData.getMeasurement(3L, testData.getRefDate().minusDays(60),
				testData.getRefDate().minusDays(40), 30F));
		assertEquals(expected, measurementList.getFirstMeasurementOfMonth(YearMonth.of(2021, 7)));

		expected = Optional.of(testData.getMeasurement(4L, testData.getRefDate().minusDays(40), null, 40F));
		assertEquals(expected, measurementList.getFirstMeasurementOfMonth(YearMonth.of(2021, 8)));

		expected = Optional.empty();
		assertEquals(expected, measurementList.getFirstMeasurementOfMonth(YearMonth.of(2021, 9)));
	}
	
	@Test
	void testGetLastMeasurementOfMonth() {
		Optional<Measurement> expected = Optional.empty();
		assertEquals(expected, measurementList.getLastMeasurementOfMonth(YearMonth.of(2021, 5)));

		expected = Optional.of(testData.getMeasurement(2L, testData.getRefDate().minusDays(80),
				testData.getRefDate().minusDays(60), 20F));
		assertEquals(expected, measurementList.getLastMeasurementOfMonth(YearMonth.of(2021, 6)));

		expected = Optional.of(testData.getMeasurement(3L, testData.getRefDate().minusDays(60),
				testData.getRefDate().minusDays(40), 30F));
		assertEquals(expected, measurementList.getLastMeasurementOfMonth(YearMonth.of(2021, 7)));

		expected = Optional.of(testData.getMeasurement(4L, testData.getRefDate().minusDays(40), null, 40F));
		assertEquals(expected, measurementList.getLastMeasurementOfMonth(YearMonth.of(2021, 8)));

		expected = Optional.empty();
		assertEquals(expected, measurementList.getLastMeasurementOfMonth(YearMonth.of(2021, 9)));
	}

	@Test
	void testGetAllMeasurementsOfMonth() {
		List<Measurement> expectedList = new ArrayList<>();
		assertEquals(expectedList, measurementList.getAllMeasurementOfMonth(YearMonth.of(2021, 5)));
		
		expectedList.add(testData.getMeasurement(1L, testData.getRefDate().minusDays(100),
				testData.getRefDate().minusDays(80), 10F));
		expectedList.add(testData.getMeasurement(2L, testData.getRefDate().minusDays(80),
				testData.getRefDate().minusDays(60), 20F));
		assertEquals(expectedList, measurementList.getAllMeasurementOfMonth(YearMonth.of(2021, 6)));
		
		expectedList.clear();
		expectedList.add(testData.getMeasurement(3L, testData.getRefDate().minusDays(60),
				testData.getRefDate().minusDays(40), 30F));
		assertEquals(expectedList, measurementList.getAllMeasurementOfMonth(YearMonth.of(2021, 7)));
		
		expectedList.clear();
		expectedList.add(testData.getMeasurement(4L, testData.getRefDate().minusDays(40), null, 40F));
		assertEquals(expectedList, measurementList.getAllMeasurementOfMonth(YearMonth.of(2021, 8)));
		
		expectedList.clear();
		assertEquals(expectedList, measurementList.getAllMeasurementOfMonth(YearMonth.of(2021, 9)));
	}
}

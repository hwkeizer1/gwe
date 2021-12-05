package nl.gwe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.MeasurementList;
import nl.gwe.datalists.MonthUsageList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
import nl.gwe.domain.YearUsage;
import nl.gwe.util.TestData;

@Slf4j
class MonthUsageServiceTest {
	
	@Mock
	MeasurementList measurementList;
	
	@Mock
	MonthUsageList monthUsageList;
	
	@InjectMocks
	MonthUsageService monthUsageService;
	
	TestData testData;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		testData = new TestData();
//		measurementList.setObservableMeasurementList(testData.getOrderedMeasurementList());
	}
	
	@Test
	void testGetLastMonthUsageYearMonth_HappyPath() {
		when(monthUsageList.getLastMonthUsageYearMonth()).thenReturn(Optional.of(YearMonth.of(2021, 4)));
		when(measurementList.getFirstMeasurementDate()).thenReturn(Optional.of(LocalDate.of(2021, 6, 2)));
		Optional<YearMonth> expected = Optional.of(YearMonth.of(2021, 4));
		assertEquals(expected, monthUsageService.getLastMonthUsageYearMonth());
	}
	
	@Test
	void testGetLastMonthUsageYearMonth_NoLastMonthUsage() {
		when(monthUsageList.getLastMonthUsageYearMonth()).thenReturn(Optional.empty());
		when(measurementList.getFirstMeasurementDate()).thenReturn(Optional.of(LocalDate.of(2021, 6, 2)));
		Optional<YearMonth> expected = Optional.of(YearMonth.of(2021, 6));
		assertEquals(expected, monthUsageService.getLastMonthUsageYearMonth());
	}
	
	@Test
	void testGetLastMonthUsageYearMonth_NoLastMonthUsage_FirstDateFirstOfMonth() {
		when(monthUsageList.getLastMonthUsageYearMonth()).thenReturn(Optional.empty());
		when(measurementList.getFirstMeasurementDate()).thenReturn(Optional.of(LocalDate.of(2021, 6, 1)));
		Optional<YearMonth> expected = Optional.of(YearMonth.of(2021, 5));
		assertEquals(expected, monthUsageService.getLastMonthUsageYearMonth());
	}
	
	@Test
	void testGetLastMonthUsageYearMonth_NoLastMonthUsage_NoFirstDate() {
		when(monthUsageList.getLastMonthUsageYearMonth()).thenReturn(Optional.empty());
		when(measurementList.getFirstMeasurementDate()).thenReturn(Optional.empty());
		Optional<YearMonth> expected = Optional.empty();
		assertEquals(expected, monthUsageService.getLastMonthUsageYearMonth());
	}
	
	@Test
	void testgetDaysOfPeriod() {
		
		// Within a month
		LocalDate start = LocalDate.of(2021, 06, 1);
		LocalDate end = LocalDate.of(2021, 06, 20);
		assertEquals(20, monthUsageService.getDaysOfPeriod(start, end));
		
		// Cross month border
		start = LocalDate.of(2021, 06, 20);
		end = LocalDate.of(2021, 07, 2);
		assertEquals(13, monthUsageService.getDaysOfPeriod(start, end));
		
		// Cross month border
		start = LocalDate.of(2021, 06, 1);
		end = LocalDate.of(2021, 07, 2);
		assertEquals(32, monthUsageService.getDaysOfPeriod(start, end));
		
		// End date before begin date
		start = LocalDate.of(2021, 07, 2);
		end = LocalDate.of(2021, 06, 1);
		assertEquals(-30, monthUsageService.getDaysOfPeriod(start, end));

		// End date equal to begin date
		start = LocalDate.of(2021, 07, 2);
		end = LocalDate.of(2021, 07, 2);
		assertEquals(1, monthUsageService.getDaysOfPeriod(start, end));
		
		
		start = LocalDate.of(2021, 07, 1);
		end = LocalDate.of(2021, 07, 31);
		assertEquals(31, monthUsageService.getDaysOfPeriod(start, end));
		
		start = LocalDate.of(2021, 03, 1);
		end = LocalDate.of(2021, 04, 30);
		assertEquals(61, monthUsageService.getDaysOfPeriod(start, end));
	}
	
	@Test
	void testGetMeasurementDaysInYearMonthAll() {
		LocalDate firstDay = LocalDate.of(2021, 4, 1);
		LocalDate lastDay = LocalDate.of(2021, 4, 30);
		
		// Complete within
		Measurement measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 3), LocalDate.of(2021, 4, 5), 10F);
		assertEquals(3, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// On firstDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 19), 10F);
		assertEquals(19, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// Before firstDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 2, 28), LocalDate.of(2021, 4, 19), 10F);
		assertEquals(19, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// On lastDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 27), LocalDate.of(2021, 4, 30), 10F);
		assertEquals(4, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// After lastDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 27), LocalDate.of(2021, 5, 1), 10F);
		assertEquals(4, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// Before firstDay and after lastDay
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 3, 27), LocalDate.of(2021, 5, 1), 10F);
		assertEquals(30, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
	}
	
	@Test
	void testCalculateAverageDayUsageOfMeasurement() {
		Measurement measurement = testData.getMeasurement(1l, LocalDate.of(2021, 7, 12), LocalDate.of(2021, 7, 19), 10F);
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F/8F);
		assertEquals(expectedMeterValues, monthUsageService.calculateAverageDayUsageOfMeasurement(measurement));
		
		measurement = testData.getMeasurement(1l, LocalDate.of(2021, 7, 12), LocalDate.of(2021, 7, 14), 11F);
		expectedMeterValues = testData.getMeterValues(null, 11F/3F);
		assertEquals(expectedMeterValues, monthUsageService.calculateAverageDayUsageOfMeasurement(measurement));
	}
	
	@Test
	void testCalculateMeasurementMonthUsagePart() {
		
		// Within a month
		Measurement measurement = testData.getMeasurement(1l, LocalDate.of(2021, 7, 12), LocalDate.of(2021, 7, 19), 10F);
		LocalDate firstDay = LocalDate.of(2021, 7, 1);
		LocalDate lastDay = LocalDate.of(2021, 7, 31);
		
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay));
		
		// Partly before month with whole number
		measurement = testData.getMeasurement(1l, LocalDate.of(2021, 6, 21), LocalDate.of(2021, 7, 10), 10F);
		firstDay = LocalDate.of(2021, 7, 1);
		lastDay = LocalDate.of(2021, 7, 31);
		
		expectedMeterValues = testData.getMeterValues(null, (10F/20F) * 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay));
		
		// Partly before month with fractional number
		measurement = testData.getMeasurement(1l, LocalDate.of(2021, 6, 20), LocalDate.of(2021, 7, 10), 10F);
		firstDay = LocalDate.of(2021, 7, 1);
		lastDay = LocalDate.of(2021, 7, 31);
		
		expectedMeterValues = testData.getMeterValues(null, (10F/21F) * 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay));
		
		// Partly after month with fractional number
		measurement = testData.getMeasurement(1l, LocalDate.of(2021, 7, 29), LocalDate.of(2021, 8, 10), 10F);
		firstDay = LocalDate.of(2021, 7, 1);
		lastDay = LocalDate.of(2021, 7, 31);
		
		expectedMeterValues = testData.getMeterValues(null, (3F/13F) * 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay));
		
		// Partly before and partly after month with fractional number
		measurement = testData.getMeasurement(1l, LocalDate.of(2021, 6, 12), LocalDate.of(2021, 8, 11), 10F);
		firstDay = LocalDate.of(2021, 7, 1);
		lastDay = LocalDate.of(2021, 7, 31);
		
		expectedMeterValues = testData.getMeterValues(null, (31F/61F) * 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay));
	}
	
	@Test
	void testAddMeterValues() {
		MeterValues mv1 = testData.getMeterValues(null, 10F);
		MeterValues mv2 = testData.getMeterValues(null, 10F);
		MeterValues expectedMeterValues = testData.getMeterValues(null, 20F);
		assertEquals(expectedMeterValues, monthUsageService.addMeterValues(mv1, mv2));
		
		mv1 = testData.getMeterValues(null, 1.339F);
		mv2 = testData.getMeterValues(null, 10.661F);
		expectedMeterValues = testData.getMeterValues(null, 12F);
		assertEquals(expectedMeterValues, monthUsageService.addMeterValues(mv1, mv2));
		
		mv1 = testData.getMeterValues(null, 10F);
		mv2 = testData.getMeterValues(null, -12F);
		expectedMeterValues = testData.getMeterValues(null, -2F);
		assertEquals(expectedMeterValues, monthUsageService.addMeterValues(mv1, mv2));
	}
	
	@Test
	void testCalculateUsagesForMonth_OneMeasurementOverFullMonth() {
		List<Measurement> measurements = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(2021, 4);
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 30), 10F));
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateUsagesForMonth(measurements, yearMonth));
	}
	
	@Test
	void testCalculateUsagesForMonth_TwoMeasurementOverFullMonth() {
		List<Measurement> measurements = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(2021, 4);
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 15), 5F));
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 4, 15), LocalDate.of(2021, 4, 30), 5F));
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateUsagesForMonth(measurements, yearMonth));
	}
	
	@Test
	void testCalculateUsagesForMonth_TwoMeasurementFromBeforeMonthToMonthEnd() {
		List<Measurement> measurements = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(2021, 4);
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 3, 17), LocalDate.of(2021, 4, 15), 10F));
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 4, 15), LocalDate.of(2021, 4, 30), 5F));
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateUsagesForMonth(measurements, yearMonth));
	}
	
	@Test
	void testCalculateUsagesForMonth_TwoMeasurementFromBeforeMonthUntilAfterMonth() {
		List<Measurement> measurements = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(2021, 4);
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 3, 17), LocalDate.of(2021, 4, 15), 10F));
		measurements.add(testData.getMeasurement(1L, LocalDate.of(2021, 4, 15), LocalDate.of(2021, 5, 16), 10F));
		MeterValues expectedMeterValues = testData.getMeterValues(null, 10F);
		assertEquals(expectedMeterValues, monthUsageService.calculateUsagesForMonth(measurements, yearMonth));
	}
}

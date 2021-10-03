package nl.gwe.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.gwe.datalists.MeasurementList;
import nl.gwe.datalists.MonthUsageList;
import nl.gwe.domain.Measurement;
import nl.gwe.repositories.MeasurementRepository;
import nl.gwe.repositories.MonthUsageRepository;
import nl.gwe.util.TestData;

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
		LocalDate start = LocalDate.of(2021, 06, 1);
		LocalDate end = LocalDate.of(2021, 06, 20);
		assertEquals(20, monthUsageService.getDaysOfPeriod(start, end));
		
		start = LocalDate.of(2021, 06, 20);
		end = LocalDate.of(2021, 07, 2);
		assertEquals(13, monthUsageService.getDaysOfPeriod(start, end));
		
		start = LocalDate.of(2021, 06, 1);
		end = LocalDate.of(2021, 07, 2);
		assertEquals(32, monthUsageService.getDaysOfPeriod(start, end));
		
		start = LocalDate.of(2021, 07, 2);
		end = LocalDate.of(2021, 06, 1);
		assertEquals(-30, monthUsageService.getDaysOfPeriod(start, end));

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
		Measurement measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 3), LocalDate.of(2021, 4, 5), 10);
		assertEquals(3, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// On firstDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 1), LocalDate.of(2021, 4, 19), 10);
		assertEquals(19, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// Before firstDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 2, 28), LocalDate.of(2021, 4, 19), 10);
		assertEquals(19, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// On lastDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 27), LocalDate.of(2021, 4, 30), 10);
		assertEquals(4, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// After lastDay boundery
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 4, 27), LocalDate.of(2021, 5, 1), 10);
		assertEquals(4, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
		
		// Before firstDay and after lastDay
		measurement = testData.getMeasurement(1L, LocalDate.of(2021, 3, 27), LocalDate.of(2021, 5, 1), 10);
		assertEquals(30, monthUsageService.getMeasurementDaysInYearMonth(measurement, firstDay, lastDay));
	}

}

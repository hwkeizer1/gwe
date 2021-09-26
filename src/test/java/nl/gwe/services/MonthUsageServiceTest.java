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
import nl.gwe.repositories.MeasurementRepository;
import nl.gwe.repositories.MonthUsageRepository;

class MonthUsageServiceTest {
	
	@Mock
	MeasurementList measurementList;
	
	@Mock
	MonthUsageList monthUsageList;
	
	@InjectMocks
	MonthUsageService monthUsageService;
	
//	TestData testData;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
//		testData = new TestData();
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

}

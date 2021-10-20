package nl.gwe.datalists;

import static org.junit.jupiter.api.Assertions.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.MonthUsage;
import nl.gwe.repositories.MonthUsageRepository;
import nl.gwe.util.TestData;

@Slf4j
class MonthUsageListTest {

	@Mock
	MonthUsageRepository monthUsageRepository;

	@InjectMocks
	MonthUsageList monthUsageList;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		testData = new TestData();
	}

	TestData testData;

	@Test
	void testGetLastMonthUsageOfYear() {
		monthUsageList.setObservableMonthUsageList(testData.getOrderedMonthUsageListComplete());
		Optional<MonthUsage> expectedMonthUsage = Optional
				.of(testData.getMonthUsage(12L, YearMonth.of(testData.getRefYear(), 12), 100.1F + 12));
		assertEquals(expectedMonthUsage, monthUsageList.getLastMonthUsageOfYear(testData.getRefYear()));
	}

	@Test
	void testGetFirstMonthUsageOfYear() {
		monthUsageList.setObservableMonthUsageList(testData.getOrderedMonthUsageListComplete());
		Optional<MonthUsage> expectedMonthUsage = Optional
				.of(testData.getMonthUsage(1L, YearMonth.of(testData.getRefYear(), 1), 100.1F + 1));
		assertEquals(expectedMonthUsage, monthUsageList.getFirstMonthUsageOfYear(testData.getRefYear()));
	}

	@Test
	void testGetAllMonthUsageOfYear() {
		monthUsageList.setObservableMonthUsageList(testData.getOrderedMonthUsageListComplete());
		List<MonthUsage> expectedMonthUsage = testData.getOrderedMonthUsageListComplete();
		assertEquals(expectedMonthUsage, monthUsageList.getAllMonthUsageOfYear(testData.getRefYear()));
	}

}

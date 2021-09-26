package nl.gwe.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import org.springframework.stereotype.Service;

import javafx.collections.ListChangeListener;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.MeasurementList;
import nl.gwe.datalists.MonthUsageList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MonthUsage;

@Slf4j
@Service
public class MonthUsageService implements ListChangeListener<Measurement> {

	private final MeasurementList measurementList;
	private final MonthUsageList monthUsageList;

	public MonthUsageService(MonthUsageList monthUsageList, MeasurementList measurementList) {
		this.monthUsageList = monthUsageList;
		this.measurementList = measurementList;
		this.measurementList.addListener(this);
	}

	@Override
	public void onChanged(Change<? extends Measurement> c) {
		Optional<LocalDate> lastMeasurementDate = measurementList.getLastMeasurementDate();
		Optional<YearMonth> lastMonthUsageYearMonth = getLastMonthUsageYearMonth();
		log.debug("lastMonthUsageYearMonth: {}", lastMonthUsageYearMonth);
		if (lastMeasurementDate.isPresent() && lastMonthUsageYearMonth.isPresent()) {
			if (lastMonthUsageYearMonth.get().atEndOfMonth().isBefore(lastMeasurementDate.get().plusMonths(1))) {
				monthUsageList.add(calculateNewMonthUsage(lastMonthUsageYearMonth.get()));
			}
		}
	}
	
	MonthUsage calculateNewMonthUsage(YearMonth lastMonthUsageYearMonth) {
		log.debug("Calculating NewMonthUsage {}", lastMonthUsageYearMonth);
		return new MonthUsage();
	}

	Optional<YearMonth> getLastMonthUsageYearMonth() {
		Optional<YearMonth> lastMonth = monthUsageList.getLastMonthUsageYearMonth();
		Optional<LocalDate> firstDate = measurementList.getFirstMeasurementDate();
		log.debug("lastMonth {}", lastMonth);
		log.debug("firstDate {}", firstDate);
		if (!lastMonth.isPresent()) {
			if (firstDate.isPresent()) {
				if (firstDate.get().getDayOfMonth() == 1) {
					return Optional
							.of(YearMonth.of(firstDate.get().getYear(), firstDate.get().getMonth()));
				} else {
					return Optional.of(YearMonth.of(firstDate.get().getYear(), firstDate.get().getMonth()).plusMonths(1));
				}
			}
			return Optional.empty();
		}
		return lastMonth;
	}

}

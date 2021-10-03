package nl.gwe.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import javafx.collections.ListChangeListener;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.MeasurementList;
import nl.gwe.datalists.MonthUsageList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
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
		if (lastMeasurementDate.isPresent() && lastMonthUsageYearMonth.isPresent()) {
			if (lastMonthUsageYearMonth.get().atEndOfMonth().plusMonths(1).isBefore(lastMeasurementDate.get())) {
				monthUsageList.add(calculateNewMonthUsage(lastMonthUsageYearMonth.get()));
			}
		}
	}

	MonthUsage calculateNewMonthUsage(YearMonth lastMonthUsageYearMonth) {
		MonthUsage monthUsage = new MonthUsage();
		monthUsage.setDate(lastMonthUsageYearMonth.plusMonths(1));
		List<Measurement> measurements = getMeasurementsForLastMonthCalculation(lastMonthUsageYearMonth);
		monthUsage.setUsages(calculateUsagesForMonth(measurements, lastMonthUsageYearMonth.plusMonths(1)));

		return monthUsage;
	}

	MeterValues calculateUsagesForMonth(List<Measurement> measurements, YearMonth yearMonth) {
		MeterValues meterValues = new MeterValues();
		LocalDate firstDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
		LocalDate lastDay = yearMonth.atEndOfMonth();
		for (Measurement measurement: measurements) {
			
		}
		return meterValues;
	}
	
//	MeterValues calculateMeasurementMonthUsagePart(Measurement measurement, LocalDate firstDay, LocalDate lastDay) {
//		Long numberOfDaysTotal = getDaysOfPeriod(measurement.getStartDate(), measurement.getEndDate());
//		if (measurement.getStartDate().isBefore(firstDay)) {
//			MeterValue measurementDayUsage = 
//			// calculate average usages for this period meterValues
//			// calculate number of days in the required yearMonth periode
//			// Usages = numberOfDays * average usages (meterValues)
//		}
//	}
	
	Long getMeasurementDaysInYearMonth(Measurement measurement, LocalDate firstDay, LocalDate lastDay) {
		if (measurement.getStartDate().isBefore(firstDay)) {
			if (measurement.getEndDate().isAfter(lastDay)) {
				return getDaysOfPeriod(firstDay, lastDay);
			} else {
				return getDaysOfPeriod(firstDay, measurement.getEndDate());
			}		
		} else {
			if (measurement.getEndDate().isBefore(lastDay)) {
				return getDaysOfPeriod(measurement.getStartDate(), measurement.getEndDate());
			} else {
				return getDaysOfPeriod(measurement.getStartDate(), lastDay);
			}
		}
	}
	
	Long getDaysOfPeriod(LocalDate startDate, LocalDate endDate) {
		return ChronoUnit.DAYS.between(startDate, endDate) + 1;
	}

	Optional<YearMonth> getLastMonthUsageYearMonth() {
		Optional<YearMonth> lastMonth = monthUsageList.getLastMonthUsageYearMonth();
		Optional<LocalDate> firstDate = measurementList.getFirstMeasurementDate();
		if (!lastMonth.isPresent()) {
			if (firstDate.isPresent()) {
				if (firstDate.get().getDayOfMonth() == 1) {
					return Optional.of(YearMonth.of(firstDate.get().getYear(), firstDate.get().getMonth()).minusMonths(1));
				} else {
					return Optional
							.of(YearMonth.of(firstDate.get().getYear(), firstDate.get().getMonth()));
				}
			}
			return Optional.empty();
		}
		return lastMonth;
	}

	public List<Measurement> getMeasurementsForLastMonthCalculation(YearMonth lastMonthUsage) {
		List<Measurement> measurements = new ArrayList<>();
		measurementList.getLastMeasurementOfMonth(lastMonthUsage).ifPresent(measurements::add);
		if (measurements.isEmpty())
			throw new RuntimeException("Error getting the last measurement of month " + lastMonthUsage);
		measurements.addAll(measurementList.getAllMeasurementOfMonth(lastMonthUsage.plusMonths(1)));
		int count = measurements.size();
		measurementList.getFirstMeasurementOfMonth(lastMonthUsage.plusMonths(2)).ifPresent(measurements::add);
		if (measurements.size() != count + 1)
			throw new RuntimeException("Error getting the first measurement of month " + lastMonthUsage.plusMonths(2));
		return measurements;
	}

}

package nl.gwe.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

	public ObservableList<MonthUsage> getReadOnlyMonthUsageList() {
		return monthUsageList.getReadOnlyMonthUsageList();
	}
	
	@Override
	public void onChanged(Change<? extends Measurement> c) {
		Optional<LocalDate> lastMeasurementDate = measurementList.getLastMeasurementDate();
		Optional<YearMonth> lastMonthUsageYearMonth = getLastMonthUsageYearMonth();
		while (lastMeasurementDate.isPresent() && lastMonthUsageYearMonth.isPresent()
				&& lastMonthUsageYearMonth.get().plusMonths(1).atEndOfMonth().isBefore(lastMeasurementDate.get())) {
			monthUsageList.add(calculateNewMonthUsage(lastMonthUsageYearMonth.get()));
			lastMonthUsageYearMonth = getLastMonthUsageYearMonth();
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
			MeterValues monthUsagePart = calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay);
			meterValues = addMeterValues(meterValues, monthUsagePart);
		}
		return meterValues;
	}
	
	MeterValues addMeterValues(MeterValues mv1, MeterValues mv2) {
		MeterValues result = new MeterValues();
		result.addMeterValues(mv1);
		result.addMeterValues(mv2);
		return result;
	}
	
	MeterValues calculateMeasurementMonthUsagePart(Measurement measurement, LocalDate firstDay, LocalDate lastDay) {
		Long numberOfDaysTotal = getDaysOfPeriod(measurement.getStartDate(), measurement.getEndDate());
		if (measurement.getStartDate().isBefore(firstDay) || measurement.getEndDate().isAfter(lastDay)) {
			MeterValues measurementDayUsage = calculateAverageDayUsageOfMeasurement(measurement);
			Long numberOfDaysInMonth = getMeasurementDaysInYearMonth(measurement, firstDay, lastDay);
			return calculateTotalUsage(measurementDayUsage, numberOfDaysInMonth);
		} else {
			MeterValues measurementDayUsage = calculateAverageDayUsageOfMeasurement(measurement);
			return calculateTotalUsage(measurementDayUsage, numberOfDaysTotal);
		}
	}
	
	MeterValues calculateTotalUsage(MeterValues meterValues, Long numberOfDays) {
		MeterValues totalUsage = new MeterValues();
		totalUsage.setLowElectricityPurchased(meterValues.getLowElectricityPurchased()*numberOfDays);
		totalUsage.setLowElectricityDelivered(meterValues.getLowElectricityDelivered()*numberOfDays);
		totalUsage.setHighElectricityPurchased(meterValues.getHighElectricityPurchased()*numberOfDays);
		totalUsage.setHighElectricityDelivered(meterValues.getHighElectricityDelivered()*numberOfDays);
		totalUsage.setGasPurchased(meterValues.getGasPurchased()*numberOfDays);
		totalUsage.setWaterPurchased(meterValues.getWaterPurchased()*numberOfDays);
		return totalUsage;
	}
	
	MeterValues calculateAverageDayUsageOfMeasurement(Measurement measurement) {
		MeterValues averageDayUsage = new MeterValues();
		Long numberOfDays = getDaysOfPeriod(measurement.getStartDate(), measurement.getEndDate());
		averageDayUsage.setLowElectricityPurchased(measurement.getUsages().getLowElectricityPurchased()/numberOfDays);
		averageDayUsage.setLowElectricityDelivered(measurement.getUsages().getLowElectricityDelivered()/numberOfDays);
		averageDayUsage.setHighElectricityPurchased(measurement.getUsages().getHighElectricityPurchased()/numberOfDays);
		averageDayUsage.setHighElectricityDelivered(measurement.getUsages().getHighElectricityDelivered()/numberOfDays);
		averageDayUsage.setGasPurchased(measurement.getUsages().getGasPurchased()/numberOfDays);
		averageDayUsage.setWaterPurchased(measurement.getUsages().getWaterPurchased()/numberOfDays);
		return averageDayUsage;
	}
	
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
		int count = 0;
		while (measurements.isEmpty()) {
			measurementList.getLastMeasurementOfMonth(lastMonthUsage.minusMonths(++count)).ifPresent(measurements::add);
			if (count == 12) {
				throw new RuntimeException("To much data between measurements, please provide more data");
			}
		}
		measurements.addAll(measurementList.getAllMeasurementOfMonth(lastMonthUsage.plusMonths(1)));
		return measurements;
	}

	public List<MonthUsage> getMonthUsagesOfYear(Integer year) {
		List<MonthUsage> monthUsages = new ArrayList<>();
		if (getLastMonthUsageOfYear(year - 1).isPresent()) {
			monthUsages.add(getLastMonthUsageOfYear(year - 1).get());
		}
		monthUsages.addAll(getAllMonthUsageOfYear(year));
		if (getFirstMonthUsageOfYear(year + 1).isPresent()) {
			monthUsages.add(getFirstMonthUsageOfYear(year + 1).get());
		}
		return monthUsages;
	}
	
	Optional<MonthUsage> getLastMonthUsageOfYear(Integer year) {
		return monthUsageList.getLastMonthUsageOfYear(year);
	}
	
	Optional<MonthUsage> getFirstMonthUsageOfYear(Integer year) {
		return monthUsageList.getFirstMonthUsageOfYear(year);
	}
	
	List<MonthUsage> getAllMonthUsageOfYear(Integer year) {
		return monthUsageList.getAllMonthUsageOfYear(year);
	}

}

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
		log.debug("Calculating usages for month {} with first day {} and last day {}", yearMonth, firstDay, lastDay);
		for (Measurement measurement: measurements) {
			log.debug("\tProcessing measurement {}", measurement);
			MeterValues monthUsagePart = calculateMeasurementMonthUsagePart(measurement, firstDay, lastDay);
			log.debug("\tMonth usage part: {}", monthUsagePart);
			meterValues = addMeterValues(meterValues, monthUsagePart);
			log.debug("\tResulting metervalues: {}", meterValues);
		}
		return meterValues;
	}
	
	MeterValues addMeterValues(MeterValues mv1, MeterValues mv2) {
		MeterValues result = new MeterValues();
		result.setLowElectricityPurchased(mv1.getLowElectricityPurchased() + mv2.getLowElectricityPurchased());
		result.setLowElectricityDelivered(mv1.getLowElectricityDelivered() + mv2.getLowElectricityDelivered());
		result.setHighElectricityPurchased(mv1.getHighElectricityPurchased() + mv2.getHighElectricityPurchased());
		result.setHighElectricityDelivered(mv1.getHighElectricityDelivered() + mv2.getHighElectricityDelivered());
		result.setGasPurchased(mv1.getGasPurchased() + mv2.getGasPurchased());
		result.setWaterPurchased(mv1.getWaterPurchased() + mv2.getWaterPurchased());
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

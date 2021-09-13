package nl.gwe.datalists;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MonthUsage;
import nl.gwe.repositories.MonthUsageRepository;

@Slf4j
@Component
public class MonthUsageList implements ListChangeListener<Measurement> {
	
	private final MeasurementList measurementList;
	private final MonthUsageRepository monthUsageRepository;

	private ObservableList<MonthUsage> observableMonthUsageList;

	public MonthUsageList(MeasurementList measurementlist, MonthUsageRepository monthUsageRepository) {
		this.measurementList = measurementlist;
		this.monthUsageRepository = monthUsageRepository;
		
		// Listen to observableMeasurementList for changes
		this.measurementList.addListener(this);
		
		this.observableMonthUsageList = FXCollections.observableList(monthUsageRepository.findAll());
	}
	
	public ObservableList<MonthUsage> getReadOnlyMonthUsageList() {
		return FXCollections.unmodifiableObservableList(observableMonthUsageList);
	}

	@Override
	public void onChanged(Change<? extends Measurement> c) {
		LocalDate lastMeasurement = measurementList.getLastMeasurementDate();
		if (lastMeasurement != null) {
			checkForNewMonthUsage(lastMeasurement);
		}
		
	}
	
	private void checkForNewMonthUsage(LocalDate lastMeasurementDate) {
		Optional<LocalDate> optionalDate = getLastMonthUsage();
		if (optionalDate.isPresent()) {
			LocalDate lastMonthUsage = optionalDate.get();
			LocalDate fullMonthAfterUsage = lastMonthUsage.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(1);
			if (lastMeasurementDate.isAfter(fullMonthAfterUsage)) calculateNewMonthUsage(lastMonthUsage);
		} else {/* nog geen lastMonthUsage beschikbaar */}
	}
	

	private void calculateNewMonthUsage(LocalDate lastMonthUsage) {
		List<Measurement> measurements = measurementList.getAllMeasurementsForLastMonthCalculation(lastMonthUsage);
		
	}
	
	private Optional<LocalDate> getLastMonthUsage() {
		return (monthUsageRepository.findAll().stream()
				.map(MonthUsage::getDate)
				.max(LocalDate::compareTo));
				
	}
}

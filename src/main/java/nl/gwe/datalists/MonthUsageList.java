package nl.gwe.datalists;

import java.time.YearMonth;
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
public class MonthUsageList  {
	
	private final MonthUsageRepository monthUsageRepository;

	private ObservableList<MonthUsage> observableMonthUsageList;

	public MonthUsageList(MeasurementList measurementlist, MonthUsageRepository monthUsageRepository) {
		this.monthUsageRepository = monthUsageRepository;
		this.observableMonthUsageList = FXCollections.observableList(monthUsageRepository.findAll());
	}
	
	public ObservableList<MonthUsage> getReadOnlyMonthUsageList() {
		return FXCollections.unmodifiableObservableList(observableMonthUsageList);
	}
	
	public Optional<YearMonth> getLastMonthUsageYearMonth() {
		return (monthUsageRepository.findAll().stream()
				.map(MonthUsage::getDate)
				.max(YearMonth::compareTo));		
	}

	public void add(MonthUsage monthUsage) {
		log.debug("Saved NewMonthUsage: {}", monthUsage);
		observableMonthUsageList.add(monthUsage);
		monthUsageRepository.saveAll(observableMonthUsageList);
	}
	
	public void addListener(ListChangeListener<MonthUsage> listener) {
		observableMonthUsageList.addListener(listener);
	}
	
	public void removeChangeListener(ListChangeListener<MonthUsage> listener) {
		observableMonthUsageList.removeListener(listener);
	}
	
	/*
	 * Setter for JUnit testing only
	 */
	void setObservableMonthUsageList(ObservableList<MonthUsage> observableMonthUsageList) {
		this.observableMonthUsageList = observableMonthUsageList;
	}
}

package nl.gwe.datalists;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.MonthUsage;
import nl.gwe.repositories.MonthUsageRepository;

@Slf4j
@Component
public class MonthUsageList  {
	
	private final MonthUsageRepository monthUsageRepository;

	private ObservableList<MonthUsage> observableMonthUsageList;
	private ObservableList<Integer> observableYearList;

	public MonthUsageList(MonthUsageRepository monthUsageRepository) {
		this.monthUsageRepository = monthUsageRepository;
		this.observableMonthUsageList = FXCollections.observableList(monthUsageRepository.findAll());
		this.observableYearList = FXCollections.observableList(getYears());
	}
	
	public ObservableList<MonthUsage> getReadOnlyMonthUsageList() {
		return FXCollections.unmodifiableObservableList(observableMonthUsageList);
	}
	
	public List<Integer> getReadOnlyYearList() {
		return FXCollections.unmodifiableObservableList(observableYearList);
	}
	
	public Optional<YearMonth> getFirstMonthUsageYearMonth() {
		return (monthUsageRepository.findAll().stream()
				.map(MonthUsage::getDate)
				.findFirst());
	}
	public Optional<YearMonth> getLastMonthUsageYearMonth() {
		return (monthUsageRepository.findAll().stream()
				.map(MonthUsage::getDate)
				.max(YearMonth::compareTo));		
	}
	
	public Optional<MonthUsage> getLastMonthUsageOfYear(Integer year) {
		return observableMonthUsageList.stream()
				.filter(m -> m.getDate().equals(YearMonth.of(year, 12))).findFirst();
	}
	
	public Optional<MonthUsage> getFirstMonthUsageOfYear(Integer year) {
		return observableMonthUsageList.stream()
				.filter(m -> m.getDate().equals(YearMonth.of(year, 1))).findFirst();
	}
	
	public List<MonthUsage> getAllMonthUsageOfYear(Integer year) {
		return observableMonthUsageList.stream()
				.filter(m -> m.getDate().getYear() == year).collect(Collectors.toList());
	}
	
	private List<Integer> getYears() {
		List<Integer> years = new ArrayList<>();
		if (getFirstMonthUsageYearMonth().isPresent() && getLastMonthUsageYearMonth().isPresent()) {
			Integer startYear = getFirstMonthUsageYearMonth().get().getYear();
			Integer endYear = getLastMonthUsageYearMonth().get().getYear();
			
			for (int i = startYear; i <= endYear; i++ ) {
				years.add(i);
			}
		}
		return years;
	}
	

	public void add(MonthUsage monthUsage) {
		observableMonthUsageList.add(monthUsage);
		if (!observableYearList.contains(monthUsage.getDate().getYear())) {
			observableYearList.add(monthUsage.getDate().getYear());
		}
		monthUsageRepository.saveAll(observableMonthUsageList);
	}
	
	public void addListener(ListChangeListener<Integer> listener) {
		observableYearList.addListener(listener);
	}
	

	public void removeChangeListener(ListChangeListener<Integer> listener) {
		observableYearList.removeListener(listener);
	}
	
	/*
	 * Setter for JUnit testing only
	 */
	void setObservableMonthUsageList(ObservableList<MonthUsage> observableMonthUsageList) {
		this.observableMonthUsageList = observableMonthUsageList;
	}
}

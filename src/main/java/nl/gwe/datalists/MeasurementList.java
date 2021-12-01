package nl.gwe.datalists;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j
@Component
public class MeasurementList {
	
	private final MeasurementRepository measurementRepository;
	
	private ObservableList<Measurement> observableMeasurementList;

	public MeasurementList(MeasurementRepository measurementRepository) {
		this.measurementRepository = measurementRepository;
		observableMeasurementList = FXCollections.observableList(this.measurementRepository.findAll());
	}
	
	public ObservableList<Measurement> getReadOnlyMeasurementList() {
		return FXCollections.unmodifiableObservableList(observableMeasurementList);
	}
	
	public Optional<Measurement> getLastMeasurement() {
		return observableMeasurementList.stream()
				.filter(m -> m.getEndDate() == null).findAny();
	}
	
	public Optional<LocalDate> getLastMeasurementDate() {
		Optional<Measurement> optionalMeasurement = getLastMeasurement();
		if (optionalMeasurement.isPresent()) return Optional.of(optionalMeasurement.get().getStartDate());
		return Optional.empty();
	}
	
	public Optional<Measurement> getFirstMeasurement() {
		return observableMeasurementList.stream()
				.filter(m -> m.getEndDate() != null).findFirst();
	}
	
	public Optional<LocalDate> getFirstMeasurementDate() {
		Optional<Measurement> optionalMeasurement = getFirstMeasurement();
		if (optionalMeasurement.isPresent()) return Optional.of(optionalMeasurement.get().getStartDate());
		return Optional.empty();
	}
	
	public Optional<Measurement> getFirstMeasurementOfMonth(YearMonth yearMonth) {
		return observableMeasurementList.stream()
				.filter(m -> m.getStartDate().getYear() == (yearMonth.getYear()))
				.filter(m -> m.getStartDate().getMonth().equals(yearMonth.getMonth()))
				.findFirst();
	}
	
	public Optional<Measurement> getLastMeasurementOfMonth(YearMonth yearMonth) {
		return observableMeasurementList.stream()
				.filter(m -> m.getStartDate().getYear() == (yearMonth.getYear()))
				.filter(m -> m.getStartDate().getMonth().equals(yearMonth.getMonth()))
				.max(Measurement::compareTo);
	}
	
	public List<Measurement> getAllMeasurementOfMonth(YearMonth yearMonth) {
		return observableMeasurementList.stream()
				.filter(m -> m.getStartDate().getYear() == (yearMonth.getYear()))
				.filter(m -> m.getStartDate().getMonth().equals(yearMonth.getMonth()))
				.collect(Collectors.toList());
	}
	
	public void add(Measurement... measurements) {
		for(Measurement measurement: measurements) {
			if (observableMeasurementList.contains(measurement)){
				observableMeasurementList.set(observableMeasurementList.lastIndexOf(measurement), measurement);
			} else {
				observableMeasurementList.add(measurement);
			}
		}
		measurementRepository.saveAll(observableMeasurementList);
	}
	
	public void addListener(ListChangeListener<Measurement> listener) {
		observableMeasurementList.addListener(listener);
	}
	
	public void removeChangeListener(ListChangeListener<Measurement> listener) {
		observableMeasurementList.removeListener(listener);
	}
	
	/*
	 * Setter for JUnit testing only
	 */
	void setObservableMeasurementList(ObservableList<Measurement> observableMeasurementList) {
		this.observableMeasurementList = observableMeasurementList;
	}
}

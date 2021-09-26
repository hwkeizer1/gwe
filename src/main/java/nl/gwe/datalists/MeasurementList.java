package nl.gwe.datalists;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MonthUsage;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j
@Component
public class MeasurementList {
	
	private final MeasurementRepository measurementRepository;
	
	private ObservableList<Measurement> observableMeasurementList;

	public MeasurementList(MeasurementRepository measurementRepository) {
		this.measurementRepository = measurementRepository;
		this.observableMeasurementList = FXCollections.observableList(measurementRepository.findAll());
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
	
//	public List<Measurement> getAllMeasurementsForLastMonthCalculation(LocalDate lastMonthUsage) {
//		List<Measurement> resultList = new ArrayList<>();
//		long count = observableMeasurementList.stream()
//				.filter(m -> m.getStartDate().getMonthValue() == lastMonthUsage.getMonthValue())
//				.count();
//		Optional<Measurement> optionalFirstMeasurement = observableMeasurementList.stream()
//				.filter(m -> m.getStartDate().getMonthValue() == lastMonthUsage.getMonthValue())
//				.skip(count-1)
//				.findFirst();
//		if (!optionalFirstMeasurement.isPresent()) {
//			return resultList;
//		}
//		resultList.add(optionalFirstMeasurement.get());
//		LocalDate firstDate = optionalFirstMeasurement.get().getStartDate();
//		resultList.addAll(observableMeasurementList.stream()
//				.filter(m -> m.getStartDate().isAfter(firstDate))
//				.collect(Collectors.toList()));
//		//Lijst moet firstdate bevatten (laatste meeting maand ervoor) tot en MET de laatste meeting (tweede meeting maand erna)
//		return resultList;
//	}
	
	
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

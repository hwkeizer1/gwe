package nl.gwe.datalists;

import java.util.List;
import java.util.Set;

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
		this.observableMeasurementList = FXCollections.observableList(measurementRepository.findAll());
		
		// DEBUG, should be removed
		observableMeasurementList.addListener((ListChangeListener<Measurement>)c -> {log.debug("Detected a change! ");});
	}
	
	public ObservableList<Measurement> getReadOnlyMeasurementList() {
		return FXCollections.unmodifiableObservableList(observableMeasurementList);
	}
	
	public void add(Measurement... measurements) {
		for(Measurement measurement: measurements) {
			if (observableMeasurementList.contains(measurement)){
				observableMeasurementList.remove(measurement);
			}
		}
		observableMeasurementList.addAll(measurements);
		measurementRepository.saveAll(observableMeasurementList);
	}
}

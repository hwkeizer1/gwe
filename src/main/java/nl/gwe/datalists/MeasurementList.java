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
	
	
	private ObservableList<Measurement> measurementList;

	public MeasurementList(MeasurementRepository measurementRepository) {
		this.measurementRepository = measurementRepository;
		this.measurementList = FXCollections.observableList(measurementRepository.findAll());
		
		// DEBUG, should be removed
		measurementList.addListener(new ListChangeListener<Measurement>() {	 
            @Override
            public void onChanged(ListChangeListener.Change<? extends Measurement> change) {
                System.out.println("Detected a change! ");
            }
        });
	}
	
	public ObservableList<Measurement> getReadOnlyMeasurementList() {
		return FXCollections.unmodifiableObservableList(measurementList);
	}
	
	public void add(Measurement... measurements) {
		for(Measurement measurement: measurements) {
			if (measurementList.contains(measurement)){
				measurementList.remove(measurement);
			}
		}
		measurementList.addAll(measurements);
		measurementRepository.saveAll(measurementList);
	}
}

package nl.gwe.datalists;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import nl.gwe.domain.Measurement;
import nl.gwe.repositories.MeasurementRepository;

@Component
public class MeasurementList {
	
	private final MeasurementRepository measurementRepository;
	
	private ObservableList<Measurement> measurementList;

	public MeasurementList(MeasurementRepository measurementRepository) {
		this.measurementRepository = measurementRepository;
		this.measurementList = FXCollections.observableList(measurementRepository.findAll());
		
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
			measurementList.add(measurement);
		}
		measurementRepository.saveAll(measurementList);
	}
	
	public void remove(Measurement measurement) {
		measurementList.remove(measurement);
		measurementRepository.delete(measurement);
	}
	
}

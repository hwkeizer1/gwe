package nl.gwe.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import nl.gwe.datalists.MeasurementList;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j
@Service
public class MeasurementService {
	
	private final MeasurementList measurementList;

	public MeasurementService(MeasurementList measurementList) {
		this.measurementList = measurementList;
	}

	public ObservableList<Measurement> getReadOnlyMeasurementList() {
		return measurementList.getReadOnlyMeasurementList();
	}
	
	public void submit(LocalDate date, MeterValues meterValues) {
		Optional<Measurement> optionalLastMeasurement = measurementList.getLastMeasurement();
		if (optionalLastMeasurement.isPresent()) {
			Measurement previousMeasurement = optionalLastMeasurement.get();
			if (date.isBefore(previousMeasurement.getStartDate())) {
				invalidMeasurement();
				return;
			}
			if (date.isEqual(previousMeasurement.getStartDate())) {
				measurementList.add(updateMeasurement(previousMeasurement, meterValues));
				return;
			}

			measurementList.add(completeMeasurement(previousMeasurement, date.minusDays(1), meterValues),
					createMeasurement(date, meterValues));
		} else {
			log.warn("Geen eerdere meetgegevens gevonden");
			measurementList.add(createMeasurement(date, meterValues));
		}
	}
	
	private Measurement createMeasurement(LocalDate date, MeterValues meterValues) {
		Measurement measurement = new Measurement();
		measurement.setStartDate(date);
		measurement.setMeterValues(meterValues);
		return measurement;
	}
	
	private Measurement updateMeasurement(Measurement previousMeasurement, MeterValues meterValues) {
		log.warn("Datum is gelijk aan die van de laatste meeting. Waarden van de laatste meeting worden overschreven");
		meterValues.setId(previousMeasurement.getMeterValues().getId());
		previousMeasurement.setMeterValues(meterValues);
		return previousMeasurement;
	}
	
	/*
	 * Currently only absorbs invalid input from the measurement form dialog in a RuntimeException
	 * This will be changed later to produce informative feedback to the user
	 */
	private void invalidMeasurement() {
		log.warn("Datum van de meeting kan niet voor een eerdere meeting liggen!");
		// TODO sent feedback to user
		throw new RuntimeException("Onjuiste meetdatum, waarden worden genegeerd");
	}
	
	/*
	 * Completes a previous measurement with the calculated usages
	 */
	private Measurement completeMeasurement(Measurement previousMeasurement, LocalDate endDate, MeterValues endValues) {
		previousMeasurement.setEndDate(endDate);
		MeterValues usages = calculateUsage(previousMeasurement.getMeterValues(), endValues);
		previousMeasurement.setUsages(usages);
		return previousMeasurement;
	}
	
	private MeterValues calculateUsage(MeterValues m1, MeterValues m2) {
		return new MeterValues.Builder()
				.setLowElectricityPurchased(m2.getLowElectricityPurchased() - m1.getLowElectricityPurchased())
				.setLowElectricityDelivered(m2.getLowElectricityDelivered() - m1.getLowElectricityDelivered())
				.setHighElectricityPurchased(m2.getHighElectricityPurchased() - m1.getHighElectricityPurchased())
				.setHighElectricityDelivered(m2.getHighElectricityDelivered() - m1.getHighElectricityDelivered())
				.setGasPurchased(m2.getGasPurchased() - m1.getGasPurchased())
				.setWaterPurchased(m2.getWaterPurchased() - m1.getWaterPurchased())
				.build();
	}

}

package nl.gwe.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.gwe.domain.Measurement;
import nl.gwe.domain.MeterValues;
import nl.gwe.repositories.MeasurementRepository;

@Slf4j

@Service
public class MeasurementService {

	private final MeasurementRepository measurementRepository;

	public MeasurementService(MeasurementRepository measurementRepository) {
		this.measurementRepository = measurementRepository;
	}

	public void submit(MeterValues meterValues, LocalDate date) {
		Measurement measurement = new Measurement();
		Optional<Measurement> optionalLastMeasurement = getLastMeasurement();
		if (optionalLastMeasurement.isPresent()) {
			Measurement lastMeasurement = optionalLastMeasurement.get();
			log.debug("{}", lastMeasurement);
			if (date.isBefore(lastMeasurement.getStartDate())) {
				log.warn("Datum van de meeting kan niet voor de laatste meeting liggen!");
				throw new RuntimeException("Onjuiste meetdatum, waarden worden genegeerd");
			}
			if (date.isEqual(lastMeasurement.getStartDate())) {
				log.warn("Datum is gelijk aan die van de laatste meeting. Waarden van de laatste meeting worden overschreven");
				replaceMeterValues(lastMeasurement, meterValues);
				return;
			}
			lastMeasurement.setEndDate(date.minusDays(1));
			measurement.setStartDate(date);
			measurement.setMeterValues(meterValues);
			measurementRepository.save(lastMeasurement);
			measurementRepository.save(measurement);
		} else {
			log.warn("Geen eerdere meetgegevens gevonden");
			measurement.setStartDate(date);
			measurement.setMeterValues(meterValues);
			measurementRepository.save(measurement);
		}


	}

	/**
	 * The last measurement is the measurement that has no end date
	 * 
	 * @return last measurement
	 */
	private Optional<Measurement> getLastMeasurement() {
		return measurementRepository.findAll().stream()
				.filter(m -> m.getEndDate() == null).findAny();

	}
	
	private void replaceMeterValues(Measurement measurement, MeterValues meterValues) {
		Long id = measurement.getMeterValues().getId();
		measurement.setMeterValues(meterValues);
		measurement.getMeterValues().setId(id);
		measurementRepository.save(measurement);
	}
}

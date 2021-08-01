package nl.gwe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.gwe.domain.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long>{

}

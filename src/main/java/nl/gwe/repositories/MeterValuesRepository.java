package nl.gwe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.gwe.domain.MeterValues;

@Repository
public interface MeterValuesRepository extends JpaRepository<MeterValues, Long> {

}

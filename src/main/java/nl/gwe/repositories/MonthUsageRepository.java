package nl.gwe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.gwe.domain.MonthUsage;

public interface MonthUsageRepository extends JpaRepository<MonthUsage, Long> {}

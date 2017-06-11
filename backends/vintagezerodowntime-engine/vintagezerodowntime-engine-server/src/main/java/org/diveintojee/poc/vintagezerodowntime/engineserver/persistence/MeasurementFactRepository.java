package org.diveintojee.poc.vintagezerodowntime.engineserver.persistence;

import org.diveintojee.poc.vintagezerodowntime.engineserver.domain.MeasurementFact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementFactRepository extends JpaRepository<MeasurementFact, Long> {
    MeasurementFact findFirstByBusinessIdOrderByInsertedAtDesc(String businessId);
}

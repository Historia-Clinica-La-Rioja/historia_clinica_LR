package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.BodyTemperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyTemperatureRepository extends JpaRepository<BodyTemperature, Short> {}
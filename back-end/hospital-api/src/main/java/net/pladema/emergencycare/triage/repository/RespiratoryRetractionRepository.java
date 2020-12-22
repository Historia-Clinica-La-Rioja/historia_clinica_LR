package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.RespiratoryRetraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespiratoryRetractionRepository extends JpaRepository<RespiratoryRetraction, Short> {}
package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.TriageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageDetailsRepository extends JpaRepository<TriageDetails, Integer> {}
package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository;

import net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.entity.ValidatedMedicationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidatedMedicationRequestRepository extends JpaRepository<ValidatedMedicationRequest, Integer> {
}

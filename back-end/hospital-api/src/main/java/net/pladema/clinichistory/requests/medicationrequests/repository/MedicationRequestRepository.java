package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRequestRepository extends JpaRepository<MedicationRequest, Integer> {

}

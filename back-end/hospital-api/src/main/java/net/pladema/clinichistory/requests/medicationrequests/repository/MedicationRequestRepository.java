package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;

import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRequestRepository extends SGXAuditableEntityJPARepository<MedicationRequest, Integer> {

}

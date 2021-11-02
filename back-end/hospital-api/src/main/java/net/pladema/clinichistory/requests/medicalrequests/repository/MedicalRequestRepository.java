package net.pladema.clinichistory.requests.medicalrequests.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.medicalrequests.repository.entity.MedicalRequest;

import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRequestRepository extends SGXAuditableEntityJPARepository<MedicalRequest, Integer> {

}

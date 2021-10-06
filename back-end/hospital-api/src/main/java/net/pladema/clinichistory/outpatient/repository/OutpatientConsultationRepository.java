package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultation;

import org.springframework.stereotype.Repository;

@Repository
public interface OutpatientConsultationRepository extends SGXAuditableEntityJPARepository<OutpatientConsultation, Integer> {

}
package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NursingConsultationRepository extends SGXAuditableEntityJPARepository<NursingConsultation, Integer> {
}


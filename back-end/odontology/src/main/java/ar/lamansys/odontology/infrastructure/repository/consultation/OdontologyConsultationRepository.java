package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OdontologyConsultationRepository extends SGXAuditableEntityJPARepository<OdontologyConsultation, Integer> {
}

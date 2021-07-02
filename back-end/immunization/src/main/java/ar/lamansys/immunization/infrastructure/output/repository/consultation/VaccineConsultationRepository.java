package ar.lamansys.immunization.infrastructure.output.repository.consultation;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineConsultationRepository extends SGXAuditableEntityJPARepository<VaccineConsultation, Integer> {

}
package ar.lamansys.virtualConsultation.infrastructure.output.repository;

import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VirtualConsultation;

@Repository
public interface VirtualConsultationRepository extends SGXAuditableEntityJPARepository<VirtualConsultation, Integer> {


}

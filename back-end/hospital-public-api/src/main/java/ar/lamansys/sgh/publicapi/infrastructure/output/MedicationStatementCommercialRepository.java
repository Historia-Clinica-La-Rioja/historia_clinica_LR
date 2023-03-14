package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.stereotype.Repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

@Repository
public interface MedicationStatementCommercialRepository extends SGXAuditableEntityJPARepository<MedicationStatementCommercial, Integer> {
}

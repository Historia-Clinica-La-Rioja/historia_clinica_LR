package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPercentiles;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

@Repository
public interface DocumentPercentilesRepository extends SGXAuditableEntityJPARepository<DocumentPercentiles, Integer> {
}

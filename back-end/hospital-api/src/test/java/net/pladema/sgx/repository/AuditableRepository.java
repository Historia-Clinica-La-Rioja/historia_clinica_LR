package net.pladema.sgx.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditableRepository extends SGXAuditableEntityJPARepository<AuditableClass, Integer> {
}

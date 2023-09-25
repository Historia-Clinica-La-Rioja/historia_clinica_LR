package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalGroup;

import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionalGroupRepository extends SGXAuditableEntityJPARepository<InstitutionalGroup, Integer> {
}

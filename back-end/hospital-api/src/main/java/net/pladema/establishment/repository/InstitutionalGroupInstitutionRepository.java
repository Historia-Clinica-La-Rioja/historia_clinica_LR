package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalGroupInstitution;

import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionalGroupInstitutionRepository extends SGXAuditableEntityJPARepository<InstitutionalGroupInstitution, Integer> {
}

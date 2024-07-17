package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;

import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionalParameterizedFormRepository extends SGXAuditableEntityJPARepository<InstitutionalParameterizedForm, Integer> {
}

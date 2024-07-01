package net.pladema.parameterizedform.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.stereotype.Repository;

@Repository
public interface ParameterizedFormRepository extends SGXAuditableEntityJPARepository<ParameterizedForm, Integer> {

}

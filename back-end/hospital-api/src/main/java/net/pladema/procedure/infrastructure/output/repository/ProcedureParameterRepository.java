package net.pladema.procedure.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureParameterRepository extends SGXAuditableEntityJPARepository<ProcedureParameter, Integer> {
	
}

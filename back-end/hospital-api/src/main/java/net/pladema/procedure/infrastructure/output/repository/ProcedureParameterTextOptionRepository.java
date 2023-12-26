package net.pladema.procedure.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterTextOption;

import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureParameterTextOptionRepository extends SGXAuditableEntityJPARepository<ProcedureParameterTextOption, Integer> {

}

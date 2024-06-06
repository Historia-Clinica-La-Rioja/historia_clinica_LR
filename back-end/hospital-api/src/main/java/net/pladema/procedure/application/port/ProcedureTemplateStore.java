package net.pladema.procedure.application.port;

import net.pladema.procedure.domain.EProcedureTemplateStatusBo;

import java.util.Optional;

public interface ProcedureTemplateStore {
	Optional<EProcedureTemplateStatusBo> findParameterStatus(Integer procedureTemplateId);

	void updateStatus(Integer procedureTemplateId, EProcedureTemplateStatusBo nextState);
}

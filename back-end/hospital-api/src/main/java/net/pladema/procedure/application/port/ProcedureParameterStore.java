package net.pladema.procedure.application.port;

import java.util.Optional;

import net.pladema.procedure.domain.ProcedureParameterBo;

public interface ProcedureParameterStore {
	public Optional<ProcedureParameterBo> findById(Integer id);
	public Optional<ProcedureParameterBo> findByProcedureTemplateIdAndOrder(Integer id, Short order);
	void updateOrder(Integer id, Short orderNumber);
}

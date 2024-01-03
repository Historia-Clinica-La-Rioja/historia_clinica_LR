package net.pladema.procedure.application.port;

import net.pladema.procedure.domain.ProcedureParameterBo;

import java.util.Optional;

public interface ProcedureParameterStore {
	public Optional<ProcedureParameterBo> findById(Integer id);
	public Optional<ProcedureParameterBo> findByProcedureTemplateIdAndOrder(Integer id, Short order);
	void updateOrder(Integer id, Short orderNumber);
}

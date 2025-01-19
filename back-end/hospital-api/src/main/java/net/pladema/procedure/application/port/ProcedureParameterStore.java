package net.pladema.procedure.application.port;

import java.util.List;
import java.util.Optional;

import net.pladema.procedure.domain.ProcedureParameterBo;

public interface ProcedureParameterStore {
	Optional<ProcedureParameterBo> findById(Integer id);
	Optional<ProcedureParameterBo> findByProcedureTemplateIdAndOrder(Integer id, Short order);
	void updateOrder(Integer id, Short orderNumber);
	public List<ProcedureParameterBo> findByIds(List<Integer> ids);
}

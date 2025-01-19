package net.pladema.procedure.application;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureParameterStore;
import net.pladema.procedure.domain.ProcedureParameterBo;

@Service
@RequiredArgsConstructor
public class FindProcedureParameter {
	private final ProcedureParameterStore procedureParameterStore;
	public List<ProcedureParameterBo> run(List<Integer> procedureParameterIds) {
		return procedureParameterStore.findByIds(procedureParameterIds);
	}
}
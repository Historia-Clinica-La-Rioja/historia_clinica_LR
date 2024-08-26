package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.ProcedureParameterStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.ProcedureParameterBo;

import net.pladema.procedure.application.FindProcedureParameter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcedureParameterStorageImpl implements ProcedureParameterStorage {

	private final FindProcedureParameter findProcedureParameter;
	@Override
	public List<ProcedureParameterBo> findProcedureParametersById(List<Integer> parameterIds) {
		return findProcedureParameter
		.run(parameterIds)
		.stream()
		.map(this::mapToProcedureParameterBo)
		.collect(Collectors.toList());
	}

	private ProcedureParameterBo mapToProcedureParameterBo(net.pladema.procedure.domain.ProcedureParameterBo src) {
		return new ProcedureParameterBo(
			src.getId(),
			src.getProcedureTemplateId(),
			src.getLoincId(),
			src.getOrderNumber(),
			src.getTypeId(),
			src.getInputCount(),
			src.isNumeric(),
			src.isSnomed()
		);
	}
}

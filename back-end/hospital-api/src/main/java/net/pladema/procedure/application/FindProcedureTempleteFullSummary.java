package net.pladema.procedure.application;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.domain.fullsummary.ProcedureTemplateFullSummaryVo;

import net.pladema.procedure.application.exceptions.ProcedureTemplateNotFoundException;
import net.pladema.procedure.application.port.ProcedureTemplateStore;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProcedureTempleteFullSummary {
	private final ProcedureTemplateStore procedureTemplateStore;

	public ProcedureTemplateFullSummaryVo run(Integer procedureTemplateId) throws ProcedureTemplateNotFoundException{
		return procedureTemplateStore
		.findFullSummaryById(procedureTemplateId)
		.orElseThrow(() -> new ProcedureTemplateNotFoundException(procedureTemplateId));
	}
}

package net.pladema.procedure.application.port;

import net.pladema.procedure.domain.EProcedureTemplateStatusBo;

import net.pladema.procedure.domain.ProcedureTemplateVo;

import java.util.Optional;
import java.util.List;

import net.pladema.procedure.domain.fullsummary.ProcedureTemplateFullSummaryVo;

public interface ProcedureTemplateStore {
	Optional<EProcedureTemplateStatusBo> findParameterStatus(Integer procedureTemplateId);

	void updateStatus(Integer procedureTemplateId, EProcedureTemplateStatusBo nextState);

	public List<ProcedureTemplateVo> findAvailableForDiagnosticReport(Integer diagnosticReportId);

	Optional<ProcedureTemplateFullSummaryVo> findFullSummaryById(Integer procedureTemplateId);
}

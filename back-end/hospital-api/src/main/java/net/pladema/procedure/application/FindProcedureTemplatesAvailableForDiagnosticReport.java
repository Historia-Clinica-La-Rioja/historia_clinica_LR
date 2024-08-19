package net.pladema.procedure.application;

import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureTemplateStore;
import net.pladema.procedure.domain.ProcedureTemplateVo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindProcedureTemplatesAvailableForDiagnosticReport {
	private final ProcedureTemplateStore procedureTemplateStore;
	public List<ProcedureTemplateVo> run(Integer diagnosticReportId) {
		return procedureTemplateStore.findAvailableForDiagnosticReport(diagnosticReportId);
	}
}

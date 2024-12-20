package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDiagnosticReportPort;

import lombok.RequiredArgsConstructor;

import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SharedDiagnosticReportPortImpl implements SharedDiagnosticReportPort {

	private final GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository;

	private final CompleteDiagnosticReportService completeDiagnosticReportService;

	@Override
	public void completeDiagnosticReport(Integer serviceRequestId, Integer patientId, String notes) {
		var diagnosticReport = CompleteDiagnosticReportBo.onlyObservations(notes);
		var diagnosticReportId = getDiagnosticReportInfoRepository.getDiagnosticReportIdByServiceRequestId(serviceRequestId);
		completeDiagnosticReportService.run(patientId, diagnosticReportId, diagnosticReport, null );
	}

}

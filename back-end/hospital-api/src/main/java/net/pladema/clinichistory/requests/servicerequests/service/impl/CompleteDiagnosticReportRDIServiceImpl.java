package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportRDIService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteDiagnosticReportRDIServiceImpl implements CompleteDiagnosticReportRDIService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final LoadDiagnosticReports loadDiagnosticReports;
    private final SnomedService snomedService;
	private final AppointmentOrderImageRepository appointmentOrderImageRepository;

	@Override
	@Transactional
	public Integer run(Integer patientId, Integer appointmentId) {
		log.debug("input -> patientId {}, appointmentId {}", patientId, appointmentId);
		Optional<Integer> study = appointmentOrderImageRepository.getStudyId(appointmentId);

		if (study.isPresent()) {
			Integer studyId = study.get();
			Integer result = diagnosticReportRepository.findById(studyId).stream().mapToInt(dr -> {
						Assert.notNull(patientId, "El código identificador del paciente es obligatorio");
						assertCompleteDiagnosticReport(dr);

						DiagnosticReportBo diagnosticReportBo = getCompletedDiagnosticReport(dr);
						var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(studyId);
						return loadDiagnosticReports.run(documentDiagnosticReport.getDocumentId(), patientId, Optional.of(studyId), List.of(diagnosticReportBo)).get(0);
					})
					.findFirst()
					.orElse(-1);
			appointmentOrderImageRepository.updateStudyId(appointmentId, result);
			log.debug("Output -> {}", result);
			return result;
		}
		return -1;
	}

    private DiagnosticReportBo getCompletedDiagnosticReport(DiagnosticReport diagnosticReport) {
        log.debug("Input parameters -> diagnosticReport {} ", diagnosticReport);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.FINAL_RDI);
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        return result;
    }

	private void assertCompleteDiagnosticReport(DiagnosticReport dr){
		Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.CANCELLED), String.format("El estudio con id '%d' no se puede completar porque ha sido cancelado", dr.getId()));
	}
}

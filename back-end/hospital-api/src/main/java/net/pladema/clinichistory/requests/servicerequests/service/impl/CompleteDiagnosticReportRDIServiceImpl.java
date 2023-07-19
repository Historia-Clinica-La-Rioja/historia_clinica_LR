package net.pladema.clinichistory.requests.servicerequests.service.impl;

import java.util.List;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportRDIService;

@Service
public class CompleteDiagnosticReportRDIServiceImpl implements CompleteDiagnosticReportRDIService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final LoadDiagnosticReports loadDiagnosticReports;
    private final SnomedService snomedService;

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
    private static final Logger LOG = LoggerFactory.getLogger(CompleteDiagnosticReportRDIServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public CompleteDiagnosticReportRDIServiceImpl(DiagnosticReportRepository diagnosticReportRepository, DocumentService documentService,
												  LoadDiagnosticReports loadDiagnosticReports, SnomedService snomedService, AppointmentOrderImageRepository appointmentOrderImageRepository){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.loadDiagnosticReports = loadDiagnosticReports;
        this.snomedService = snomedService;
		this.appointmentOrderImageRepository = appointmentOrderImageRepository;
	}

    @Override
    public Integer run(Integer patientId, Integer appointmentId) {
        LOG.debug("input -> patientId {}, diagnosticReportId {}, completeDiagnosticReportBo {}", patientId, appointmentId);
		Integer studyId = appointmentOrderImageRepository.getStudyId(appointmentId)
				.orElseThrow(()->new NotFoundException("study-not-found", "Study not found"));
        Integer result = diagnosticReportRepository.findById(studyId).stream().mapToInt(dr -> {
			Assert.notNull(patientId, "El código identificador del paciente es obligatorio");
			assertCompleteDiagnosticReport(dr);

            DiagnosticReportBo diagnosticReportBo = getCompletedDiagnosticReport(dr);
            var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(studyId);
            return loadDiagnosticReports.run(documentDiagnosticReport.getDocumentId(), patientId, List.of(diagnosticReportBo)).get(0);
        }).findFirst().orElse(-1);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private DiagnosticReportBo getCompletedDiagnosticReport(DiagnosticReport diagnosticReport) {
        LOG.debug("Input parameters -> diagnosticReport {} ", diagnosticReport);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.FINAL_RDI);
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        return result;
    }

	private void assertCompleteDiagnosticReport(DiagnosticReport dr){
		Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.CANCELLED), "El estudio con id "+ dr.getId() + " no se puede completar porque ha sido cancelado");
	}
}

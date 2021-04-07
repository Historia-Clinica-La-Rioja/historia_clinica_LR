package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CompleteDiagnosticReportServiceImpl implements CompleteDiagnosticReportService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final DiagnosticReportService diagnosticReportService;
    private final SnomedService snomedService;

    private static final Logger LOG = LoggerFactory.getLogger(CompleteDiagnosticReportServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public CompleteDiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository, DocumentService documentService,
                                               DiagnosticReportService diagnosticReportService, SnomedService snomedService){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.diagnosticReportService = diagnosticReportService;
        this.snomedService = snomedService;
    }

    @Override
    public Integer run(PatientInfoBo patient, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo) {
        LOG.debug("input -> patient {}, diagnosticReportId {}, completeDiagnosticReportBo {}", patient, diagnosticReportId, completeDiagnosticReportBo);
        Integer result = diagnosticReportRepository.findById(diagnosticReportId).stream().mapToInt(dr -> {
            assertRequiredFields(patient);
            assertCompleteDiagnosticReport(dr);

            DiagnosticReportBo diagnosticReportBo = getCompletedDiagnosticReport(dr, completeDiagnosticReportBo);
            var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(diagnosticReportId);
            return diagnosticReportService.loadDiagnosticReport(documentDiagnosticReport.getDocumentId(), patient, List.of(diagnosticReportBo)).get(0);
        }).findFirst().orElse(-1);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private DiagnosticReportBo getCompletedDiagnosticReport(DiagnosticReport diagnosticReport, CompleteDiagnosticReportBo completeDiagnosticReportBo) {
        LOG.debug("Input parameters -> diagnosticReport {}, completeDiagnosticReportBo {} ", diagnosticReport, completeDiagnosticReportBo);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.FINAL);
        result.setObservations(completeDiagnosticReportBo.getObservations());
        result.setLink(completeDiagnosticReportBo.getLink());
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        return result;
    }

    private void assertRequiredFields(PatientInfoBo patient) {
        LOG.debug("Input parameters -> patient {}", patient);
        var patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(patient);
    }

    private void assertCompleteDiagnosticReport(DiagnosticReport dr){
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.FINAL), "El estudio con id "+ dr.getId() + " no se puede completar porque ya ha sido completado");
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.CANCELLED), "El estudio con id "+ dr.getId() + " no se puede completar porque ha sido cancelado");
    }
}

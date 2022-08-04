package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteDiagnosticReportService;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class DeleteDiagnosticReportServiceImpl implements DeleteDiagnosticReportService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final LoadDiagnosticReports loadDiagnosticReports;
    private final SnomedService snomedService;

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDiagnosticReportServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public DeleteDiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository,
                                             DocumentService documentService,
                                             LoadDiagnosticReports loadDiagnosticReports,
                                             SnomedService snomedService){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.loadDiagnosticReports = loadDiagnosticReports;
        this.snomedService = snomedService;
    }
    @Override
    public Integer execute(PatientInfoBo patient, Integer diagnosticReportId) {
        LOG.debug("Input: patient: {}, diagnosticReportId: {}", patient, diagnosticReportId);
        Optional<DiagnosticReport> drOpt = diagnosticReportRepository.findById(diagnosticReportId);
        if (drOpt.isPresent()){
            var dr = drOpt.get();
            assertRequiredFields(patient);
            assertDeleteDiagnosticReport(dr);

            DiagnosticReportBo diagnosticReportBo = getCancelledDiagnosticReport(dr);
            var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(diagnosticReportId);
            Integer result = loadDiagnosticReports.run(documentDiagnosticReport.getDocumentId(), patient, List.of(diagnosticReportBo)).get(0);
            LOG.trace(OUTPUT, result);
            return result;
        }
        return null;
    }

    private DiagnosticReportBo getCancelledDiagnosticReport(DiagnosticReport diagnosticReport) {
        LOG.debug("Input parameters -> diagnosticReport {}", diagnosticReport);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.CANCELLED);
        result.setNoteId(diagnosticReport.getNoteId());
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    private void assertRequiredFields(PatientInfoBo patient) {
        LOG.debug("Input parameters -> patient {}", patient);
        var patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(patient);
    }

    private void assertDeleteDiagnosticReport(DiagnosticReport dr){
        LOG.debug("Input parameters -> diagnosticReport {}", dr);
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.CANCELLED), "El estudio con id "+ dr.getId() + " no se puede cancelar debido a que ya está cancelado");
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.FINAL), "El estudio con id "+ dr.getId() + " no se puede cancelar debido a que ya ha sido completada");
    }
}

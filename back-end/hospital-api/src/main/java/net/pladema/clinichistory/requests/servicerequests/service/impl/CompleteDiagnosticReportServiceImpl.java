package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.MedicationStatementValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.sgx.files.FileService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CompleteDiagnosticReportServiceImpl implements CompleteDiagnosticReportService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final NoteService noteService;
    private final DocumentService documentService;
    private final DiagnosticReportService diagnosticReportService;
    private final SnomedService snomedService;
    private final FileService fileService;
    private static final String RELATIVE_DIRECTORY = "/patient/{patiendId}/diagnostic-reports/{studyId}";

    private static final Logger LOG = LoggerFactory.getLogger(CompleteDiagnosticReportServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public CompleteDiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository,
                                               NoteService noteService,
                                               DocumentService documentService,
                                               DiagnosticReportService diagnosticReportService, SnomedService snomedService, FileService fileService){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.noteService = noteService;
        this.documentService = documentService;
        this.diagnosticReportService = diagnosticReportService;
        this.snomedService = snomedService;
        this.fileService = fileService;
    }

    @Override
    public void run(PatientInfoBo patient, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo, MultipartFile file) {
        diagnosticReportRepository.findById(diagnosticReportId).ifPresent(dr -> {
            assertRequiredFields(patient);
            assertCompleteDiagnosticReport(dr);
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = fileService.createFileName(extension);
            String completePath = buildCompleteFilePath(patient.getId(), newFileName);
            completeDiagnosticReportBo.setFilePath(completePath);
            fileService.saveFile(completePath, file);

            DiagnosticReportBo diagnosticReportBo = getCompletedDiagnosticReport(dr, completeDiagnosticReportBo);
            var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(diagnosticReportId);
            diagnosticReportService.loadDiagnosticReport(documentDiagnosticReport.getDocumentId(), patient, List.of(diagnosticReportBo));
        });
    }

    private DiagnosticReportBo getCompletedDiagnosticReport(DiagnosticReport diagnosticReport, CompleteDiagnosticReportBo completeDiagnosticReportBo) {
        LOG.debug("Input parameters -> diagnosticReport {}, completeDiagnosticReportBo {} ", diagnosticReport, completeDiagnosticReportBo);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.FINAL);
        result.setObservations(completeDiagnosticReportBo.getObservations());
        result.setLink(completeDiagnosticReportBo.getLink());
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        result.setFilePath(completeDiagnosticReportBo.getFilePath());
        return result;
    }

    private String buildCompleteFilePath(Integer patientId, String relativeFilePath){
        LOG.debug("Input parameters -> patientId {}, relativeFilePath {}", patientId, relativeFilePath);
        String partialPath = RELATIVE_DIRECTORY
                .replace("{patiendId}", patientId.toString())
                .concat(relativeFilePath);
        String result = fileService.buildPath(partialPath);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private void assertRequiredFields(PatientInfoBo patient) {
        LOG.debug("Input parameters -> patient {}", patient);
        var patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(patient);
    }

    private void assertCompleteDiagnosticReport(DiagnosticReport dr){
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.FINAL), "El estudio con id "+ dr.getId() + " no se puede completar porque ya esta completar");
    }
}

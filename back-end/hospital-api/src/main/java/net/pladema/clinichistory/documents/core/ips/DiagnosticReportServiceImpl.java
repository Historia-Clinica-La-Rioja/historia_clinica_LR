package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiagnosticReportServiceImpl implements DiagnosticReportService {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final NoteService noteService;
    private final SnomedService snomedService;
    private final CalculateCie10CodesService calculateCie10CodesService;

    public DiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository,
                                       DocumentService documentService,
                                       NoteService noteService,
                                       SnomedService snomedService,
                                       CalculateCie10CodesService calculateCie10CodesService) {
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.noteService = noteService;
        this.snomedService = snomedService;
        this.calculateCie10CodesService = calculateCie10CodesService;
    }

    public List<DiagnosticReport> loadDiagnosticReport(Long documentId, PatientInfoBo patientInfo, List<DiagnosticReportBo> diagnosticReportBos) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, studyBo {}", documentId, patientInfo, diagnosticReportBos);
        List<DiagnosticReport> result = new ArrayList<>();
        diagnosticReportBos.forEach(diagnosticReportBo -> {
            Integer snomedId = snomedService.getSnomedId(diagnosticReportBo.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(diagnosticReportBo.getSnomed()));
            String cie10Codes = calculateCie10CodesService.execute(diagnosticReportBo.getSnomed().getSctid(), patientInfo);

            DiagnosticReport diagnosticReport = new DiagnosticReport();
            diagnosticReport.setPatientId(patientInfo.getId());

            diagnosticReport.setSnomedId(snomedId);
            diagnosticReport.setCie10Codes(cie10Codes);
            diagnosticReport.setHealthConditionId(diagnosticReportBo.getHealthConditionId());

            diagnosticReport.setNoteId(diagnosticReportBo.getNoteId() != null ?
                    diagnosticReportBo.getNoteId()
                    :
                    noteService.createNote(diagnosticReportBo.getObservations()));

            if (diagnosticReportBo.getStatusId() != null) {
                diagnosticReport.setStatusId(diagnosticReportBo.getStatusId());
            }

            result.add(diagnosticReportRepository.save(diagnosticReport));
            documentService.createDocumentDiagnosticReport(documentId, diagnosticReport.getId());
        });
        LOG.trace(OUTPUT, result);
        return result;
    }
}

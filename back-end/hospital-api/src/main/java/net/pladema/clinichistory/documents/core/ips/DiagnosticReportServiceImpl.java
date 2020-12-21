package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiagnosticReportServiceImpl implements DiagnosticReportService {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final NoteService noteService;

    public DiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository,
                                       DocumentService documentService,
                                       NoteService noteService){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.noteService = noteService;
    }

    public List<DiagnosticReportBo> loadDiagnosticReport(Long documentId, Integer patientId, List<DiagnosticReportBo> diagnosticReportBos) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, studyBo {}", documentId, patientId, diagnosticReportBos);
        diagnosticReportBos.forEach(diagnosticReportBo -> {
            DiagnosticReport diagnosticReport = new DiagnosticReport();
            diagnosticReport.setPatientId(patientId);
            diagnosticReport.setSctidCode(diagnosticReportBo.getSctidCode());
            diagnosticReport.setHealthConditionId(diagnosticReportBo.getHealthConditionId());

            diagnosticReport.setNoteId(noteService.createNote(diagnosticReportBo.getObservations()));

            diagnosticReportRepository.save(diagnosticReport);
            documentService.createDocumentDiagnosticReport(documentId, diagnosticReport.getId());
        });
        List<DiagnosticReportBo> result = diagnosticReportBos;
        LOG.debug(OUTPUT, result);
        return result;
    }
}

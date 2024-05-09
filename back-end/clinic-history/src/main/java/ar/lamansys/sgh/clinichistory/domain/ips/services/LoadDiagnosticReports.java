package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoadDiagnosticReports {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDiagnosticReports.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final NoteService noteService;
    private final SnomedService snomedService;

    public LoadDiagnosticReports(DiagnosticReportRepository diagnosticReportRepository,
                                 DocumentService documentService,
                                 NoteService noteService,
                                 SnomedService snomedService) {
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.noteService = noteService;
        this.snomedService = snomedService;
    }

	@Transactional
    public List<Integer> run(Long documentId, Integer patientId, List<DiagnosticReportBo> diagnosticReportBos) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, studyBo {}", documentId, patientId, diagnosticReportBos);
        List<Integer> result = new ArrayList<>();
		if (diagnosticReportBos != null) {
			diagnosticReportBos.forEach(diagnosticReportBo -> {
				DiagnosticReport diagnosticReport = getNewDiagnosticReport(patientId, diagnosticReportBo);
				result.add(diagnosticReportRepository.save(diagnosticReport).getId());
				diagnosticReportRepository.save(diagnosticReport);
				documentService.createDocumentDiagnosticReport(documentId, diagnosticReport.getId());
			});
		}
        LOG.trace(OUTPUT, result);
        return result;
    }

    private DiagnosticReport getNewDiagnosticReport(Integer patientId, DiagnosticReportBo diagnosticReportBo) {
		LOG.debug("Input parameters -> patientId {}, diagnosticReportBo {}", patientId, diagnosticReportBo);
		DiagnosticReport result = new DiagnosticReport();

        Integer snomedId = snomedService.getSnomedId(diagnosticReportBo.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(diagnosticReportBo.getSnomed()));
        result.setPatientId(patientId);

        result.setSnomedId(snomedId);
        result.setHealthConditionId(diagnosticReportBo.getHealthConditionId());

        result.setNoteId(generateNoteId(diagnosticReportBo.getNoteId(), diagnosticReportBo.getObservations()));

        result.setLink(diagnosticReportBo.getLink());

        if (diagnosticReportBo.getStatusId() != null) {
            result.setStatusId(diagnosticReportBo.getStatusId());
        }

		result.setUuid(UUID.randomUUID());
        LOG.debug(OUTPUT, result);
        return result;
    }

    public Long generateNoteId(Long noteId, String observations){
        if (noteId != null) {
            return noteId;
        } else {
            return observations != null ?  noteService.createNote(observations) : null;
        }
    }

}

package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadDiagnosticReports {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDiagnosticReports.class);

    public static final String OUTPUT = "Output -> {}";

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final NoteService noteService;
    private final SnomedService snomedService;
    private final CalculateCie10Facade calculateCie10Facade;

    public LoadDiagnosticReports(DiagnosticReportRepository diagnosticReportRepository,
                                 DocumentService documentService,
                                 NoteService noteService,
                                 SnomedService snomedService,
                                 CalculateCie10Facade calculateCie10Facade) {
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.noteService = noteService;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
    }

	@Transactional
    public List<Integer> run(Long documentId, PatientInfoBo patientInfo, List<DiagnosticReportBo> diagnosticReportBos) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, studyBo {}", documentId, patientInfo, diagnosticReportBos);
        List<Integer> result = new ArrayList<>();
        diagnosticReportBos.forEach(diagnosticReportBo -> {
            DiagnosticReport diagnosticReport = getNewDiagnosticReport(patientInfo, diagnosticReportBo);
            result.add(diagnosticReportRepository.save(diagnosticReport).getId());
            diagnosticReportRepository.save(diagnosticReport);
            documentService.createDocumentDiagnosticReport(documentId, diagnosticReport.getId());
        });
        LOG.trace(OUTPUT, result);
        return result;
    }

    private DiagnosticReport getNewDiagnosticReport(PatientInfoBo patientInfo, DiagnosticReportBo diagnosticReportBo) {
        DiagnosticReport result = new DiagnosticReport();

        Integer snomedId = snomedService.getSnomedId(diagnosticReportBo.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(diagnosticReportBo.getSnomed()));
        String cie10Codes = calculateCie10Facade.execute(diagnosticReportBo.getSnomed().getSctid(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));

        result.setPatientId(patientInfo.getId());

        result.setSnomedId(snomedId);
        result.setCie10Codes(cie10Codes);
        result.setHealthConditionId(diagnosticReportBo.getHealthConditionId());

        result.setNoteId(generateNoteId(diagnosticReportBo.getNoteId(), diagnosticReportBo.getObservations()));

        result.setLink(diagnosticReportBo.getLink());

        if (diagnosticReportBo.getStatusId() != null) {
            result.setStatusId(diagnosticReportBo.getStatusId());
        }
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

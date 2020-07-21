package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.ReportDocumentService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteReportService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.ips.service.domain.DocumentObservationsBo;
import net.pladema.clinichistory.ips.service.domain.GeneralHealthConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EvolutionNoteReportServiceImpl implements EvolutionNoteReportService {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteReportServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final ReportDocumentService reportDocumentService;

    private final NoteService noteService;

    public EvolutionNoteReportServiceImpl(DocumentService documentService, ReportDocumentService reportDocumentService,
                                          NoteService noteService) {
        this.documentService = documentService;
        this.reportDocumentService = reportDocumentService;
        this.noteService = noteService;
    }

    @Override
    public EvolutionNoteBo getDocument(Long documentId) {
        LOG.debug("Input parameters documentId {}", documentId);
        EvolutionNoteBo result = new EvolutionNoteBo();
        documentService.findById(documentId).ifPresent( document -> {
            result.setId(document.getId());
            result.setConfirmed(document.getStatusId().equalsIgnoreCase(DocumentStatus.FINAL));

            GeneralHealthConditionBo generalHealthConditionBo = reportDocumentService.getReportHealthConditionFromDocument(document.getId());
            result.setMainDiagnosis(generalHealthConditionBo.getMainDiagnosis());
            result.setDiagnosis(generalHealthConditionBo.getDiagnosis());
            result.setImmunizations(reportDocumentService.getReportInmunizationStateFromDocument(document.getId()));
            result.setAllergies(reportDocumentService.getReportAllergyIntoleranceStateFromDocument(document.getId()));
            result.setAnthropometricData(reportDocumentService.getReportAnthropometricDataStateFromDocument(document.getId()));
            result.setVitalSigns(reportDocumentService.getReportVitalSignStateFromDocument(document.getId()));
            result.setNotes(loadNotes(document));
        });
        LOG.debug(OUTPUT, result);
        return result;
    }

    private DocumentObservationsBo loadNotes(Document document) {
        LOG.debug("Input parameters document {}", document);
        DocumentObservationsBo result = new DocumentObservationsBo();
        if (document.getClinicalImpressionNoteId() != null)
            result.setClinicalImpressionNote(noteService.getDescriptionById(document.getClinicalImpressionNoteId()));
        if (document.getStudiesSummaryNoteId() != null)
            result.setStudiesSummaryNote(noteService.getDescriptionById(document.getStudiesSummaryNoteId()));
        if (document.getPhysicalExamNoteId() != null)
            result.setPhysicalExamNote(noteService.getDescriptionById(document.getPhysicalExamNoteId()));
        if (document.getIndicationsNoteId() != null)
            result.setIndicationsNote(noteService.getDescriptionById(document.getIndicationsNoteId()));
        if (document.getEvolutionNoteId() != null)
            result.setEvolutionNote(noteService.getDescriptionById(document.getEvolutionNoteId()));
        if (document.getCurrentIllnessNoteId() != null)
            result.setCurrentIllnessNote(noteService.getDescriptionById(document.getCurrentIllnessNoteId()));
        if (document.getOtherNoteId() != null)
            result.setOtherNote(noteService.getDescriptionById(document.getOtherNoteId()));
        LOG.debug(OUTPUT, result);
        return result;
    }
}

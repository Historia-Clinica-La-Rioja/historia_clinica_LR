package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EpicrisisServiceImpl implements EpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(EpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final NoteService noteService;


    public EpicrisisServiceImpl(DocumentService documentService, NoteService noteService) {
        this.documentService = documentService;
        this.noteService = noteService;
    }

    @Override
    public EpicrisisBo getDocument(Long documentId) {
        LOG.debug("Input parameters documentId {}", documentId);
        EpicrisisBo result = new EpicrisisBo();
        documentService.findById(documentId).ifPresent( document -> {
            result.setId(document.getId());
			result.setInitialDocumentId(document.getInitialDocumentId());
			result.setConfirmed(document.getStatusId().equals(DocumentStatus.FINAL));
			GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(document.getId());
            result.setMainDiagnosis(generalHealthConditionBo.getMainDiagnosis());
            result.setDiagnosis(generalHealthConditionBo.getDiagnosis());
            result.setFamilyHistories(generalHealthConditionBo.getFamilyHistories());
            result.setPersonalHistories(generalHealthConditionBo.getPersonalHistories());
            result.setImmunizations(documentService.getImmunizationStateFromDocument(document.getId()));
            result.setAllergies(documentService.getAllergyIntoleranceStateFromDocument(document.getId()));
            result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(document.getId()));
            result.setRiskFactors(documentService.getRiskFactorStateFromDocument(document.getId()));
			result.setMedications(documentService.getMedicationStateFromDocument(document.getId()));
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

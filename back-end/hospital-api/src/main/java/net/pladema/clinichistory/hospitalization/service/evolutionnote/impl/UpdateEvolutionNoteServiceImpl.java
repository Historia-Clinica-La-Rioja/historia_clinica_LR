package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateEvolutionNoteServiceImpl implements UpdateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final ClinicalObservationService clinicalObservationService;

    private final ImmunizationService immunizationService;

    private final ProceduresService proceduresService;

    public UpdateEvolutionNoteServiceImpl(DocumentService documentService,
                                          NoteService noteService,
                                          HealthConditionService healthConditionService,
                                          AllergyService allergyService,
                                          ClinicalObservationService clinicalObservationService,
                                          ImmunizationService immunizationService, ProceduresService proceduresService) {
        this.documentService = documentService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
        this.proceduresService = proceduresService;
    }

    @Override
    public EvolutionNoteBo updateDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, evolutionNote {}", internmentEpisodeId, patientId, evolutionNote);

        Optional<Document> optDoc = documentService.findById(evolutionNote.getId());
        optDoc.ifPresent(doc -> {
            deleteDocumentData(evolutionNote.getId());
            loadNotes(doc, Optional.ofNullable(evolutionNote.getNotes()));
            doc = documentService.save(doc);

            evolutionNote.setDiagnosis(healthConditionService.loadDiagnosis(patientId, doc.getId(), evolutionNote.getDiagnosis()));
            evolutionNote.setAllergies(allergyService.loadAllergies(patientId, doc.getId(), evolutionNote.getAllergies()));
            evolutionNote.setImmunizations(immunizationService.loadImmunization(patientId, doc.getId(), evolutionNote.getImmunizations()));
            evolutionNote.setProcedures(proceduresService.loadProcedures(patientId, doc.getId(), evolutionNote.getProcedures()));

            evolutionNote.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, doc.getId(), Optional.ofNullable(evolutionNote.getVitalSigns())));
            evolutionNote.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(evolutionNote.getAnthropometricData())));

            evolutionNote.setId(doc.getId());

            LOG.debug(OUTPUT, evolutionNote);
        });
        return evolutionNote;
    }

    private Document loadNotes(Document evolutionNote, Optional<DocumentObservationsBo> optNotes) {
        LOG.debug("Input parameters -> evolutionNote {}, notes {}", evolutionNote, optNotes);
        optNotes.ifPresent(notes -> {
            evolutionNote.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            evolutionNote.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            evolutionNote.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            evolutionNote.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            evolutionNote.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            evolutionNote.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, evolutionNote);
        return evolutionNote;
    }

    private void deleteDocumentData(Long documentId) {
        LOG.debug("Input parameters -> documentId {}", documentId);
        documentService.deleteHealthConditionHistory(documentId);
        documentService.deleteAllergiesHistory(documentId);
        documentService.deleteImmunizationsHistory(documentId);
        documentService.deleteMedicationsHistory(documentId);
        documentService.deleteObservationsVitalSignsHistory(documentId);
        documentService.deleteObservationsLabHistory(documentId);
    }
}

package net.pladema.internation.service.documents.evolutionnote.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.ips.*;
import net.pladema.internation.service.ips.domain.DocumentObservations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateEvolutionNoteServiceImpl implements UpdateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final InmunizationService inmunizationService;


    public UpdateEvolutionNoteServiceImpl(DocumentService documentService,
                                          InternmentEpisodeService internmentEpisodeService,
                                          NoteService noteService,
                                          HealthConditionService healthConditionService,
                                          AllergyService allergyService,
                                          MedicationService medicationService,
                                          ClinicalObservationService clinicalObservationService,
                                          InmunizationService inmunizationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.medicationService = medicationService;
        this.clinicalObservationService = clinicalObservationService;
        this.inmunizationService = inmunizationService;
    }

    @Override
    public EvolutionNote updateDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNote evolutionNote) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, evolutionNote {}", internmentEpisodeId, patientId, evolutionNote);

        Optional<Document> optDoc = documentService.findById(evolutionNote.getId());
        optDoc.ifPresent(doc -> {
            deleteDocumentData(evolutionNote.getId());
            //TODO anamnesisDocument.setStatusId();
            loadNotes(doc, Optional.ofNullable(evolutionNote.getNotes()));
            doc = documentService.save(doc);

            evolutionNote.setDiagnosis(healthConditionService.loadDiagnosis(patientId, doc.getId(), evolutionNote.getDiagnosis()));
            evolutionNote.setAllergies(allergyService.loadAllergies(patientId, doc.getId(), evolutionNote.getAllergies()));
            evolutionNote.setInmunizations(inmunizationService.loadInmunization(patientId, doc.getId(), evolutionNote.getInmunizations()));

            evolutionNote.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, doc.getId(), Optional.ofNullable(evolutionNote.getVitalSigns())));
            evolutionNote.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(evolutionNote.getAnthropometricData())));

            evolutionNote.setId(doc.getId());

            LOG.debug(OUTPUT, evolutionNote);
        });
        return evolutionNote;
    }

    private Document loadNotes(Document evolutionNote, Optional<DocumentObservations> optNotes) {
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
        documentService.deleteInmunizationsHistory(documentId);
        documentService.deleteMedicationsHistory(documentId);
        documentService.deleteObservationsVitalSignsHistory(documentId);
        documentService.deleteObservationsLabHistory(documentId);
    }

    private void deleteNotes(Document anamnesisDocument) {
        LOG.debug("Input parameters -> Document {}", anamnesisDocument);
        List<Long> notesToDelete = new ArrayList<>();
        if (anamnesisDocument.getClinicalImpressionNoteId() != null)
            notesToDelete.add(anamnesisDocument.getClinicalImpressionNoteId());
        if (anamnesisDocument.getCurrentIllnessNoteId() != null)
            notesToDelete.add(anamnesisDocument.getCurrentIllnessNoteId());
        if (anamnesisDocument.getEvolutionNoteId() != null)
            notesToDelete.add(anamnesisDocument.getEvolutionNoteId());
        if (anamnesisDocument.getIndicationsNoteId() != null)
            notesToDelete.add(anamnesisDocument.getIndicationsNoteId());
        if (anamnesisDocument.getPhysicalExamNoteId() != null)
            notesToDelete.add(anamnesisDocument.getPhysicalExamNoteId());
        if (anamnesisDocument.getStudiesSummaryNoteId() != null)
            notesToDelete.add(anamnesisDocument.getStudiesSummaryNoteId());
        if (anamnesisDocument.getOtherNoteId() != null)
            notesToDelete.add(anamnesisDocument.getOtherNoteId());
        noteService.deleteAllNotes(notesToDelete);
    }



}

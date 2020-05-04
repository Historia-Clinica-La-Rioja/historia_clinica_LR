package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.service.InternmentEpisodeService;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.DocumentObservations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateAnamnesisServiceImpl implements UpdateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final VitalSignLabService vitalSignLabService;

    private final InmunizationService inmunizationService;


    public UpdateAnamnesisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      MedicationService medicationService,
                                      VitalSignLabService vitalSignLabService,
                                      InmunizationService inmunizationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.medicationService = medicationService;
        this.vitalSignLabService = vitalSignLabService;
        this.inmunizationService = inmunizationService;
    }

    @Override
    public Anamnesis updateAnanmesisDocument(Integer internmentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", internmentEpisodeId, patientId, anamnesis);

        Optional<Document> optDoc = documentService.findById(anamnesis.getId());
        optDoc.ifPresent(anamnesisDoc -> {
            deleteDocumentData(anamnesis.getId());
            //TODO anamnesisDocument.setStatusId();
            loadNotes(anamnesisDoc, Optional.ofNullable(anamnesis.getNotes()));
            anamnesisDoc = documentService.save(anamnesisDoc);

            anamnesis.setDiagnosis(healthConditionService.loadDiagnosis(patientId, anamnesisDoc.getId(), anamnesis.getDiagnosis()));
            anamnesis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientId, anamnesisDoc.getId(), anamnesis.getPersonalHistories()));
            anamnesis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, anamnesisDoc.getId(), anamnesis.getFamilyHistories()));
            anamnesis.setAllergies(allergyService.loadAllergies(patientId, anamnesisDoc.getId(), anamnesis.getAllergies()));
            anamnesis.setInmunizations(inmunizationService.loadInmunization(patientId, anamnesisDoc.getId(), anamnesis.getInmunizations()));
            anamnesis.setMedications(medicationService.loadMedications(patientId, anamnesisDoc.getId(), anamnesis.getMedications()));

            anamnesis.setVitalSigns(vitalSignLabService.loadVitalSigns(patientId, anamnesisDoc.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
            anamnesis.setAnthropometricData(vitalSignLabService.loadAnthropometricData(patientId, anamnesisDoc.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

            internmentEpisodeService.updateAnamnesisDocumentId(internmentEpisodeId, anamnesisDoc.getId());
            anamnesis.setId(anamnesisDoc.getId());

            LOG.debug(OUTPUT, anamnesis);
        });
        return anamnesis;
    }

    private Document loadNotes(Document anamnesisDocument, Optional<DocumentObservations> optNotes) {
        LOG.debug("Input parameters -> anamnesisDocument {}, notes {}", anamnesisDocument, optNotes);
        optNotes.ifPresent(notes -> {
            anamnesisDocument.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            anamnesisDocument.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            anamnesisDocument.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            anamnesisDocument.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            anamnesisDocument.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            anamnesisDocument.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, anamnesisDocument);
        return anamnesisDocument;
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

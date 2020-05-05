package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;
import net.pladema.internation.service.ips.domain.DocumentObservations;
import net.pladema.internation.service.ips.*;
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

    private final ClinicalObservationService clinicalObservationService;

    private final InmunizationService inmunizationService;


    public UpdateAnamnesisServiceImpl(DocumentService documentService,
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
    public Anamnesis updateDocument(Integer internmentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", internmentEpisodeId, patientId, anamnesis);

        Optional<Document> optDoc = documentService.findById(anamnesis.getId());
        optDoc.ifPresent(doc -> {
            deleteDocumentData(anamnesis.getId());
            //TODO anamnesisDocument.setStatusId();
            loadNotes(doc, Optional.ofNullable(anamnesis.getNotes()));
            doc = documentService.save(doc);

            anamnesis.setDiagnosis(healthConditionService.loadDiagnosis(patientId, doc.getId(), anamnesis.getDiagnosis()));
            anamnesis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientId, doc.getId(), anamnesis.getPersonalHistories()));
            anamnesis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, doc.getId(), anamnesis.getFamilyHistories()));
            anamnesis.setAllergies(allergyService.loadAllergies(patientId, doc.getId(), anamnesis.getAllergies()));
            anamnesis.setInmunizations(inmunizationService.loadInmunization(patientId, doc.getId(), anamnesis.getInmunizations()));
            anamnesis.setMedications(medicationService.loadMedications(patientId, doc.getId(), anamnesis.getMedications()));

            anamnesis.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, doc.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
            anamnesis.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

            internmentEpisodeService.updateAnamnesisDocumentId(internmentEpisodeId, doc.getId());
            anamnesis.setId(doc.getId());

            LOG.debug(OUTPUT, anamnesis);
        });
        return anamnesis;
    }

    private Document loadNotes(Document document, Optional<DocumentObservations> optNotes) {
        LOG.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes -> {
            document.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            document.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            document.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            document.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            document.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            document.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, document);
        return document;
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

    private void deleteNotes(Document document) {
        LOG.debug("Input parameters -> Document {}", document);
        List<Long> notesToDelete = new ArrayList<>();
        if (document.getClinicalImpressionNoteId() != null)
            notesToDelete.add(document.getClinicalImpressionNoteId());
        if (document.getCurrentIllnessNoteId() != null)
            notesToDelete.add(document.getCurrentIllnessNoteId());
        if (document.getEvolutionNoteId() != null)
            notesToDelete.add(document.getEvolutionNoteId());
        if (document.getIndicationsNoteId() != null)
            notesToDelete.add(document.getIndicationsNoteId());
        if (document.getPhysicalExamNoteId() != null)
            notesToDelete.add(document.getPhysicalExamNoteId());
        if (document.getStudiesSummaryNoteId() != null)
            notesToDelete.add(document.getStudiesSummaryNoteId());
        if (document.getOtherNoteId() != null)
            notesToDelete.add(document.getOtherNoteId());
        noteService.deleteAllNotes(notesToDelete);
    }



}

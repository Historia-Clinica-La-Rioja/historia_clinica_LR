package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.InternmentEpisodeService;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.DocumentObservations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final VitalSignLabService vitalSignLabService;

    private final InmunizationService inmunizationService;

    public CreateAnamnesisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      VitalSignLabService vitalSignLabService,
                                      InmunizationService inmunizationService,
                                      MedicationService medicationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.vitalSignLabService = vitalSignLabService;
        this.inmunizationService = inmunizationService;
        this.medicationService = medicationService;
    }

    @Override
    public Anamnesis createAnanmesisDocument(Integer intermentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", intermentEpisodeId, patientId, anamnesis);

        Document anamnesisDocument = new Document(intermentEpisodeId, anamnesis.getDocumentStatusId(), DocumentType.ANAMNESIS);
        loadNotes(anamnesisDocument, Optional.ofNullable(anamnesis.getNotes()));
        anamnesisDocument = documentService.create(anamnesisDocument);

        anamnesis.setDiagnosis(healthConditionService.loadDiagnosis(patientId, anamnesisDocument.getId(), anamnesis.getDiagnosis()));
        anamnesis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientId, anamnesisDocument.getId(), anamnesis.getPersonalHistories()));
        anamnesis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, anamnesisDocument.getId(), anamnesis.getFamilyHistories()));
        anamnesis.setAllergies(allergyService.loadAllergies(patientId, anamnesisDocument.getId(), anamnesis.getAllergies()));
        anamnesis.setInmunizations(inmunizationService.loadInmunization(patientId, anamnesisDocument.getId(), anamnesis.getInmunizations()));
        anamnesis.setMedications(medicationService.loadMedications(patientId, anamnesisDocument.getId(), anamnesis.getMedications()));

        anamnesis.setVitalSigns(vitalSignLabService.loadVitalSigns(patientId, anamnesisDocument.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
        anamnesis.setAnthropometricData(vitalSignLabService.loadAnthropometricData(patientId, anamnesisDocument.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

        internmentEpisodeService.updateAnamnesisDocumentId(intermentEpisodeId, anamnesisDocument.getId());
        anamnesis.setId(anamnesisDocument.getId());

        LOG.debug(OUTPUT, anamnesis);
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

}

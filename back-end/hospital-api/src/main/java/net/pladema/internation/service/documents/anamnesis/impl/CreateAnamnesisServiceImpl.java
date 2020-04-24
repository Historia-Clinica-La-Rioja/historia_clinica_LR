package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.*;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.DocumentObservations;
import net.pladema.internation.service.domain.ips.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final CreateVitalSignLabService createVitalSignLabService;

    private final InmunizationService inmunizationService;

    public CreateAnamnesisServiceImpl(DocumentService documentService, NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      CreateVitalSignLabService createVitalSignLabService,
                                      InmunizationService inmunizationService) {
        this.documentService = documentService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.createVitalSignLabService = createVitalSignLabService;
        this.inmunizationService = inmunizationService;
    }

    @Override
    public Anamnesis createAnanmesisDocument(Integer intermentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", intermentEpisodeId, patientId, anamnesis);

        Document anamnesisDocument = new Document(intermentEpisodeId, DocumentStatus.FINAL, DocumentType.ANAMNESIS);
        loadNotes(anamnesisDocument, Optional.ofNullable(anamnesis.getNotes()));
        anamnesisDocument = documentService.create(anamnesisDocument);

        healthConditionService.loadDiagnosis(patientId, anamnesisDocument.getId(), anamnesis.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientId, anamnesisDocument.getId(), anamnesis.getPersonalHistories());
        healthConditionService.loadFamilyHistories(patientId, anamnesisDocument.getId(), anamnesis.getFamilyHistories());

        allergyService.loadAllergies(patientId, anamnesisDocument.getId(), anamnesis.getAllergies());
 
        inmunizationService.loadInmunization(patientId, anamnesisDocument.getId(), anamnesis.getInmunizations());

        anamnesis.setVitalSigns(createVitalSignLabService.loadVitalSigns(patientId, anamnesisDocument.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
        anamnesis.setAnthropometricData(createVitalSignLabService.loadAnthropometricData(patientId, anamnesisDocument.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

        anamnesis.setId(anamnesisDocument.getId());
        LOG.debug(OUTPUT, anamnesis);
        return anamnesis;
    }

    private Document loadNotes(Document anamnesisDocument, Optional<DocumentObservations> optNotes) {
        optNotes.ifPresent(notes -> {
                setNotes(anamnesisDocument, notes);
        });
        return anamnesisDocument;
    }

    private Document setNotes(Document anamnesisDocument, DocumentObservations notes) {
        LOG.debug("Input parameters -> anamnesisDocument {}, notes {}", anamnesisDocument, notes);
        anamnesisDocument.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
        anamnesisDocument.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
        anamnesisDocument.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
        anamnesisDocument.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
        anamnesisDocument.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
        anamnesisDocument.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        LOG.debug(OUTPUT, anamnesisDocument);
        return anamnesisDocument;
    }

    private void loadMedications(List<Medication> medications) {
        //TODO
    }

}

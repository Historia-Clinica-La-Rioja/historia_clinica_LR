package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateVitalSignLabService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.DocumentObservations;
import net.pladema.internation.service.domain.ips.Inmunization;
import net.pladema.internation.service.domain.ips.Medication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final CreateVitalSignLabService createVitalSignLabService;

    public CreateAnamnesisServiceImpl(DocumentService documentService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      CreateVitalSignLabService createVitalSignLabService) {
        this.documentService = documentService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.createVitalSignLabService = createVitalSignLabService;
    }

    @Override
    public Anamnesis createAnanmesisDocument(Integer intermentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", intermentEpisodeId, patientId, anamnesis);
        Document anamnesisDocument = new Document(intermentEpisodeId, DocumentStatus.FINAL, DocumentType.ANAMNESIS);
        anamnesisDocument = setNotes(anamnesisDocument, anamnesis.getNotes());
        anamnesisDocument = documentService.create(anamnesisDocument);
        healthConditionService.loadDiagnosis(patientId, anamnesisDocument.getId(), anamnesis.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientId, anamnesisDocument.getId(), anamnesis.getPersonalHistory());
        healthConditionService.loadFamilyHistories(patientId, anamnesisDocument.getId(), anamnesis.getFamilyHistory());
        allergyService.loadAllergies(patientId, anamnesisDocument.getId(), anamnesis.getAllergy());
        anamnesis.setVitalSigns(createVitalSignLabService.loadVitalSigns(patientId, anamnesisDocument.getId(), anamnesis.getVitalSigns()));
        anamnesis.setAnthropometricData(createVitalSignLabService.loadAnthropometricData(patientId, anamnesisDocument.getId(), anamnesis.getAnthropometricData()));
        anamnesis.setId(anamnesisDocument.getId());
        LOG.debug(OUTPUT, anamnesis);
        return anamnesis;
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

    private void loadInmunizations(List<Inmunization> inmunizations) {
        //TODO
    }

    private void loadMedications(List<Medication> medications) {
        //TODO
    }


}

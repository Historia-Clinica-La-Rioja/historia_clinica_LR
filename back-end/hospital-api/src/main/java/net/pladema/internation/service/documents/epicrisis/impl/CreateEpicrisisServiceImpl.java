package net.pladema.internation.service.documents.epicrisis.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.epicrisis.CreateEpicrisisService;
import net.pladema.internation.service.documents.epicrisis.domain.Epicrisis;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.ips.*;
import net.pladema.internation.service.ips.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateEpicrisisServiceImpl implements CreateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final InmunizationService inmunizationService;

    public CreateEpicrisisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      InmunizationService inmunizationService,
                                      MedicationService medicationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.inmunizationService = inmunizationService;
        this.medicationService = medicationService;
    }

    @Override
    public Epicrisis createDocument(Integer internmentEpisodeId, Integer patientId, Epicrisis epicrisis) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, patientId {}, epicrisis {}", internmentEpisodeId, patientId, epicrisis);

        Document document = new Document(internmentEpisodeId, epicrisis.getDocumentStatusId(), DocumentType.EPICRISIS);
        loadNotes(document, Optional.ofNullable(epicrisis.getNotes()));
        document = documentService.save(document);

        HealthConditionBo mainDiagnosis = healthConditionService.getMainDiagnosisGeneralState(internmentEpisodeId);
        epicrisis.setMainDiagnosis(healthConditionService.loadMainDiagnosis(patientId, document.getId(), Optional.ofNullable(mainDiagnosis)));
        epicrisis.setDiagnosis(healthConditionService.loadDiagnosis(patientId, document.getId(), epicrisis.getDiagnosis()));
        epicrisis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientId, document.getId(), epicrisis.getPersonalHistories()));
        epicrisis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, document.getId(), epicrisis.getPersonalHistories()));
        epicrisis.setAllergies(allergyService.loadAllergies(patientId, document.getId(), epicrisis.getAllergies()));
        epicrisis.setInmunizations(inmunizationService.loadInmunization(patientId, document.getId(), epicrisis.getInmunizations()));
        epicrisis.setMedications(medicationService.loadMedications(patientId, document.getId(), epicrisis.getMedications()));

        internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisodeId, document.getId());
        epicrisis.setId(document.getId());

        LOG.debug(OUTPUT, epicrisis);
        return epicrisis;
    }

    private void loadNotes(Document document, Optional<DocumentObservations> optNotes) {
        LOG.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes -> {
            document.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            document.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            document.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            document.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            document.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            document.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
            document.setIndicationsNoteId(noteService.createNote(notes.getIndicationsNote()));
            LOG.debug("Notes saved -> {}", notes);
        });
    }

}

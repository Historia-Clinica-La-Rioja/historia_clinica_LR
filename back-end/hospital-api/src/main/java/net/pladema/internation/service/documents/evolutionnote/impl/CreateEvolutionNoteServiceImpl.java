package net.pladema.internation.service.documents.evolutionnote.impl;

import net.pladema.internation.repository.core.entity.Document;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.evolutionnote.CreateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.ips.*;
import net.pladema.internation.service.ips.domain.DocumentObservations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateEvolutionNoteServiceImpl implements CreateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final InmunizationService inmunizationService;

    public CreateEvolutionNoteServiceImpl(DocumentService documentService,
                                          InternmentEpisodeService internmentEpisodeService,
                                          NoteService noteService,
                                          HealthConditionService healthConditionService,
                                          AllergyService allergyService,
                                          ClinicalObservationService clinicalObservationService,
                                          InmunizationService inmunizationService,
                                          MedicationService medicationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.inmunizationService = inmunizationService;
        this.medicationService = medicationService;
    }

    @Override
    public EvolutionNote createDocument(Integer intermentEpisodeId, Integer patientId, EvolutionNote evolutionNote) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", intermentEpisodeId, patientId, evolutionNote);

        Document document = new Document(intermentEpisodeId, evolutionNote.getDocumentStatusId(), DocumentType.EVALUATION_NOTE);
        loadNotes(document, Optional.ofNullable(evolutionNote.getNotes()));
        document = documentService.save(document);

        evolutionNote.setDiagnosis(healthConditionService.loadDiagnosis(patientId, document.getId(), evolutionNote.getDiagnosis()));
        evolutionNote.setAllergies(allergyService.loadAllergies(patientId, document.getId(), evolutionNote.getAllergies()));
        evolutionNote.setInmunizations(inmunizationService.loadInmunization(patientId, document.getId(), evolutionNote.getInmunizations()));

        evolutionNote.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, document.getId(), Optional.ofNullable(evolutionNote.getVitalSigns())));
        evolutionNote.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, document.getId(), Optional.ofNullable(evolutionNote.getAnthropometricData())));

        internmentEpisodeService.addEvolutionNote(intermentEpisodeId, document.getId());

        evolutionNote.setId(document.getId());

        LOG.debug(OUTPUT, evolutionNote);
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

}

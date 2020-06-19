package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.ips.service.AllergyService;
import net.pladema.clinichistory.ips.service.ClinicalObservationService;
import net.pladema.clinichistory.ips.service.HealthConditionService;
import net.pladema.clinichistory.ips.service.InmunizationService;
import net.pladema.clinichistory.ips.service.domain.DocumentObservationsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final ClinicalObservationService clinicalObservationService;

    private final InmunizationService inmunizationService;

    public CreateEvolutionNoteServiceImpl(DocumentService documentService,
                                          InternmentEpisodeService internmentEpisodeService,
                                          NoteService noteService,
                                          HealthConditionService healthConditionService,
                                          AllergyService allergyService,
                                          ClinicalObservationService clinicalObservationService,
                                          InmunizationService inmunizationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.inmunizationService = inmunizationService;
    }

    @Override
    public EvolutionNoteBo createDocument(Integer internmentEpisodeId, Integer patientId, EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", internmentEpisodeId, patientId, evolutionNote);

        Document document = new Document(internmentEpisodeId, evolutionNote.getDocumentStatusId(), DocumentType.EVALUATION_NOTE);
        loadNotes(document, Optional.ofNullable(evolutionNote.getNotes()));
        document = documentService.save(document);

        evolutionNote.setDiagnosis(healthConditionService.loadDiagnosis(patientId, document.getId(), evolutionNote.getDiagnosis()));
        evolutionNote.setAllergies(allergyService.loadAllergies(patientId, document.getId(), evolutionNote.getAllergies()));
        evolutionNote.setInmunizations(inmunizationService.loadInmunization(patientId, document.getId(), evolutionNote.getInmunizations()));

        evolutionNote.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, document.getId(), Optional.ofNullable(evolutionNote.getVitalSigns())));
        evolutionNote.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, document.getId(), Optional.ofNullable(evolutionNote.getAnthropometricData())));

        internmentEpisodeService.addEvolutionNote(internmentEpisodeId, document.getId());

        evolutionNote.setId(document.getId());

        LOG.debug(OUTPUT, evolutionNote);
        return evolutionNote;
    }

    @Override
    public Long createEvolutionDiagnosis(Integer internmentEpisodeId, Integer patientId, EvolutionDiagnosisBo evolutionDiagnosis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, evolutionDiagnosis {}", internmentEpisodeId, patientId, evolutionDiagnosis);

        Document doc = new Document(internmentEpisodeId, DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE);
        loadNotes(doc, Optional.ofNullable(evolutionDiagnosis.getNotes()));
        doc = documentService.save(doc);
        List<Integer> diagnoses = healthConditionService.copyDiagnoses(evolutionDiagnosis.getDiagnosesId());
        Long result = doc.getId();
        diagnoses.forEach( id -> documentService.createDocumentHealthCondition(result, id));
        internmentEpisodeService.addEvolutionNote(internmentEpisodeId, doc.getId());

        LOG.debug(OUTPUT, result);
        return result;
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

}

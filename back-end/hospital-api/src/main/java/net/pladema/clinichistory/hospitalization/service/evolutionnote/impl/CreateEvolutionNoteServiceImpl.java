package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
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

    private final ImmunizationService immunizationService;

    private final ProceduresService proceduresService;

    public CreateEvolutionNoteServiceImpl(DocumentService documentService,
                                          InternmentEpisodeService internmentEpisodeService,
                                          NoteService noteService,
                                          HealthConditionService healthConditionService,
                                          AllergyService allergyService,
                                          ClinicalObservationService clinicalObservationService,
                                          ImmunizationService immunizationService, ProceduresService proceduresService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
        this.proceduresService = proceduresService;
    }

    @Override
    public EvolutionNoteBo createDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientInfo {}, anamnesis {}", internmentEpisodeId, patientInfo, evolutionNote);

        Document document = new Document(internmentEpisodeId, evolutionNote.getDocumentStatusId(), DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION);
        loadNotes(document, Optional.ofNullable(evolutionNote.getNotes()));

        document = documentService.save(document);

        evolutionNote.setDiagnosis(healthConditionService.loadDiagnosis(patientInfo, document.getId(), evolutionNote.getDiagnosis()));
        evolutionNote.setAllergies(allergyService.loadAllergies(patientInfo, document.getId(), evolutionNote.getAllergies()));
        evolutionNote.setImmunizations(immunizationService.loadImmunization(patientInfo, document.getId(), evolutionNote.getImmunizations()));
        evolutionNote.setProcedures(proceduresService.loadProcedures(patientInfo, document.getId(), evolutionNote.getProcedures()));

        evolutionNote.setVitalSigns(clinicalObservationService.loadVitalSigns(patientInfo, document.getId(), Optional.ofNullable(evolutionNote.getVitalSigns())));
        evolutionNote.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientInfo, document.getId(), Optional.ofNullable(evolutionNote.getAnthropometricData())));

        internmentEpisodeService.addEvolutionNote(internmentEpisodeId, document.getId());

        evolutionNote.setId(document.getId());

        LOG.debug(OUTPUT, evolutionNote);
        return evolutionNote;
    }

    @Override
    public Long createEvolutionDiagnosis(Integer internmentEpisodeId, Integer patientId, EvolutionDiagnosisBo evolutionDiagnosis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, evolutionDiagnosis {}", internmentEpisodeId, patientId, evolutionDiagnosis);

        Document doc = new Document(internmentEpisodeId, DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION);
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

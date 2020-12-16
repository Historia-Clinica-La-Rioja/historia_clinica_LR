package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.ips.AllergyService;
import net.pladema.clinichistory.documents.service.ips.HealthConditionService;
import net.pladema.clinichistory.documents.service.ips.ImmunizationService;
import net.pladema.clinichistory.documents.service.ips.MedicationService;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
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

    private final ImmunizationService immunizationService;

    public CreateEpicrisisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      ImmunizationService immunizationService,
                                      MedicationService medicationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.immunizationService = immunizationService;
        this.medicationService = medicationService;
    }

    @Override
    public EpicrisisBo createDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, EpicrisisBo epicrisis) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, patientInfo {}, epicrisis {}", internmentEpisodeId, patientInfo, epicrisis);

        Document document = new Document(internmentEpisodeId, epicrisis.getDocumentStatusId(), DocumentType.EPICRISIS, SourceType.HOSPITALIZATION);
        loadNotes(document, Optional.ofNullable(epicrisis.getNotes()));
        document = documentService.save(document);

        epicrisis.setMainDiagnosis(healthConditionService.loadMainDiagnosis(patientInfo, document.getId(), Optional.of(epicrisis.getMainDiagnosis())));
        epicrisis.setDiagnosis(healthConditionService.loadDiagnosis(patientInfo, document.getId(), epicrisis.getDiagnosis()));
        epicrisis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientInfo, document.getId(), epicrisis.getPersonalHistories()));
        epicrisis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientInfo, document.getId(), epicrisis.getFamilyHistories()));
        epicrisis.setAllergies(allergyService.loadAllergies(patientInfo, document.getId(), epicrisis.getAllergies()));
        epicrisis.setImmunizations(immunizationService.loadImmunization(patientInfo, document.getId(), epicrisis.getImmunizations()));
        epicrisis.setMedications(medicationService.loadMedications(patientInfo, document.getId(), epicrisis.getMedications()));

        internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisodeId, document.getId());
        epicrisis.setId(document.getId());

        LOG.debug(OUTPUT, epicrisis);
        return epicrisis;
    }

    private void loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
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

package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.service.*;
import net.pladema.clinichistory.ips.service.domain.DocumentObservationsBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
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

    private final ClinicalObservationService clinicalObservationService;

    private final ImmunizationService immunizationService;

    private final ProceduresService proceduresService;

    public CreateAnamnesisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      ClinicalObservationService clinicalObservationService,
                                      ImmunizationService immunizationService,
                                      ProceduresService proceduresService,
                                      MedicationService medicationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
        this.proceduresService = proceduresService;
        this.medicationService = medicationService;
    }

    @Override
    public AnamnesisBo createDocument(Integer intermentEpisodeId, Integer patientId, AnamnesisBo anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", intermentEpisodeId, patientId, anamnesis);

        Document doc = new Document(intermentEpisodeId, anamnesis.getDocumentStatusId(), DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION);
        loadNotes(doc, Optional.ofNullable(anamnesis.getNotes()));
        doc = documentService.save(doc);

        anamnesis.setMainDiagnosis(healthConditionService.loadMainDiagnosis(patientId, doc.getId(), Optional.ofNullable(anamnesis.getMainDiagnosis())));
        anamnesis.setDiagnosis(healthConditionService.loadDiagnosis(patientId, doc.getId(), anamnesis.getDiagnosis()));
        anamnesis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientId, doc.getId(), anamnesis.getPersonalHistories()));
        anamnesis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, doc.getId(), anamnesis.getFamilyHistories()));
        anamnesis.setAllergies(allergyService.loadAllergies(patientId, doc.getId(), anamnesis.getAllergies()));
        anamnesis.setImmunizations(immunizationService.loadImmunization(patientId, doc.getId(), anamnesis.getImmunizations()));
        anamnesis.setMedications(medicationService.loadMedications(patientId, doc.getId(), anamnesis.getMedications()));
        anamnesis.setProcedures(proceduresService.loadProcedures(patientId, doc.getId(), anamnesis.getProcedures()));

        anamnesis.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, doc.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
        anamnesis.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

        internmentEpisodeService.updateAnamnesisDocumentId(intermentEpisodeId, doc.getId());
        anamnesis.setId(doc.getId());

        LOG.debug(OUTPUT, anamnesis);
        return anamnesis;
    }

    private Document loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
        LOG.debug("Input parameters -> anamnesisDocument {}, notes {}", document, optNotes);
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

}

package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.patient.controller.dto.BasicPatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    private final ImmunizationService immunizationService;


    public UpdateAnamnesisServiceImpl(DocumentService documentService,
                                      InternmentEpisodeService internmentEpisodeService,
                                      NoteService noteService,
                                      HealthConditionService healthConditionService,
                                      AllergyService allergyService,
                                      MedicationService medicationService,
                                      ClinicalObservationService clinicalObservationService,
                                      ImmunizationService immunizationService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.medicationService = medicationService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
    }

    @Override
    public AnamnesisBo updateDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, AnamnesisBo anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientInfo {}, anamnesis {}", internmentEpisodeId, patientInfo, anamnesis);

        Optional<Document> optDoc = documentService.findById(anamnesis.getId());
        optDoc.ifPresent(doc -> {
            deleteDocumentData(anamnesis.getId());
            loadNotes(doc, Optional.ofNullable(anamnesis.getNotes()));
            doc = documentService.save(doc);

            anamnesis.setDiagnosis(healthConditionService.loadDiagnosis(patientInfo, doc.getId(), anamnesis.getDiagnosis()));
            anamnesis.setPersonalHistories(healthConditionService.loadPersonalHistories(patientInfo, doc.getId(), anamnesis.getPersonalHistories()));
            anamnesis.setFamilyHistories(healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), anamnesis.getFamilyHistories()));
            anamnesis.setAllergies(allergyService.loadAllergies(patientInfo, doc.getId(), anamnesis.getAllergies()));
            anamnesis.setImmunizations(immunizationService.loadImmunization(patientInfo, doc.getId(), anamnesis.getImmunizations()));
            anamnesis.setMedications(medicationService.loadMedications(patientInfo, doc.getId(), anamnesis.getMedications()));

            anamnesis.setVitalSigns(clinicalObservationService.loadVitalSigns(patientInfo, doc.getId(), Optional.ofNullable(anamnesis.getVitalSigns())));
            anamnesis.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientInfo, doc.getId(), Optional.ofNullable(anamnesis.getAnthropometricData())));

            internmentEpisodeService.updateAnamnesisDocumentId(internmentEpisodeId, doc.getId());
            anamnesis.setId(doc.getId());

            LOG.debug(OUTPUT, anamnesis);
        });
        return anamnesis;
    }

    private Document loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
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
        documentService.deleteImmunizationsHistory(documentId);
        documentService.deleteMedicationsHistory(documentId);
        documentService.deleteObservationsVitalSignsHistory(documentId);
        documentService.deleteObservationsLabHistory(documentId);
    }
}

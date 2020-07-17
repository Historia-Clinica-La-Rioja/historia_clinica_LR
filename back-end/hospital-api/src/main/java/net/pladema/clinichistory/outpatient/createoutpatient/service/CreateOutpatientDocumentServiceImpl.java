package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.service.AllergyService;
import net.pladema.clinichistory.ips.service.ClinicalObservationService;
import net.pladema.clinichistory.ips.service.HealthConditionService;
import net.pladema.clinichistory.ips.service.MedicationService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateOutpatientDocumentServiceImpl implements CreateOutpatientDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final UpdateOutpatientConsultationService updateOutpatientConsultationService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final MedicationService medicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final NoteService noteService;

    public CreateOutpatientDocumentServiceImpl(DocumentService documentService,
                                               UpdateOutpatientConsultationService updateOutpatientConsultationService,
                                               HealthConditionService healthConditionService,
                                               AllergyService allergyService,
                                               MedicationService medicationService,
                                               ClinicalObservationService clinicalObservationService,
                                               NoteService noteService) {
        this.documentService = documentService;
        this.updateOutpatientConsultationService = updateOutpatientConsultationService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.medicationService = medicationService;
        this.clinicalObservationService = clinicalObservationService;
        this.noteService = noteService;
    }


    @Override
    public OutpatientDocumentBo create(Integer outpatientId, Integer patientId,  OutpatientDocumentBo outpatient) {
        LOG.debug("Input parameters outpatientId {}, patientId {}, outpatient {}", outpatientId, outpatient);
        OutpatientDocumentBo result = new OutpatientDocumentBo();
        Document doc = new Document(outpatientId, DocumentStatus.FINAL, DocumentType.OUTPATIENT, SourceType.AMBULATORIA);
        loadNotes(doc, Optional.ofNullable(outpatient.getEvolutionNote()));
        doc = documentService.save(doc);

        outpatient.setProblems(healthConditionService.loadProblems(patientId, doc.getId(), outpatient.getProblems()));
        outpatient.setFamilyHistories(healthConditionService.loadFamilyHistories(patientId, doc.getId(), outpatient.getFamilyHistories()));
        outpatient.setMedications(medicationService.loadMedications(patientId, doc.getId(), outpatient.getMedications()));
        outpatient.setAllergies(allergyService.loadAllergies(patientId, doc.getId(), outpatient.getAllergies()));

        outpatient.setVitalSigns(clinicalObservationService.loadVitalSigns(patientId, doc.getId(), Optional.ofNullable(outpatient.getVitalSigns())));
        outpatient.setAnthropometricData(clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(outpatient.getAnthropometricData())));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatientId, doc.getId());
        result.setId(doc.getId());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Document loadNotes(Document document, Optional<String> optNotes) {
        LOG.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes -> {
            document.setOtherNoteId(noteService.createNote(optNotes.get()));
        });
        LOG.debug(OUTPUT, document);
        return document;
    }
}


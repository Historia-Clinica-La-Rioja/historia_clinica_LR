package net.pladema.clinichistory.documents.core;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.service.*;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.*;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentFactoryImpl implements DocumentFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFactoryImpl.class);

    private final DocumentService documentService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final AllergyService allergyService;

    private final CreateMedicationService createMedicationService;

    private final ClinicalObservationService clinicalObservationService;

    private final ImmunizationService immunizationService;

    private final ProceduresService proceduresService;

    private final DiagnosticReportService diagnosticReportService;

    public DocumentFactoryImpl(DocumentService documentService,
                               NoteService noteService,
                               HealthConditionService healthConditionService,
                               AllergyService allergyService,
                               ClinicalObservationService clinicalObservationService,
                               ImmunizationService immunizationService,
                               ProceduresService proceduresService,
                               CreateMedicationService createMedicationService,
                               DiagnosticReportService diagnosticReportService) {
        this.documentService = documentService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.allergyService = allergyService;
        this.clinicalObservationService = clinicalObservationService;
        this.immunizationService = immunizationService;
        this.proceduresService = proceduresService;
        this.createMedicationService = createMedicationService;
        this.diagnosticReportService = diagnosticReportService;
    }

    @Override
    public Long run(IDocumentBo documentBo) {

        Document doc = new Document(documentBo.getEncounterId(),
                documentBo.getDocumentStatusId(),
                documentBo.getDocumentType(),
                documentBo.getDocumentSource());
        loadNotes(doc, Optional.ofNullable(documentBo.getNotes()));
        doc = documentService.save(doc);

        PatientInfoBo patientInfo = documentBo.getPatientInfo();
        healthConditionService.loadMainDiagnosis(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getMainDiagnosis()));
        healthConditionService.loadDiagnosis(patientInfo, doc.getId(), documentBo.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientInfo, doc.getId(), documentBo.getPersonalHistories());
        healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), documentBo.getFamilyHistories());
        healthConditionService.loadProblems(patientInfo, doc.getId(), documentBo.getProblems());
        allergyService.loadAllergies(patientInfo, doc.getId(), documentBo.getAllergies());
        immunizationService.loadImmunization(patientInfo, doc.getId(), documentBo.getImmunizations());
        createMedicationService.execute(patientInfo, doc.getId(), documentBo.getMedications());
        proceduresService.loadProcedures(patientInfo, doc.getId(), documentBo.getProcedures());

        clinicalObservationService.loadVitalSigns(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getVitalSigns()));
        clinicalObservationService.loadAnthropometricData(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getAnthropometricData()));

        diagnosticReportService.loadDiagnosticReport(doc.getId(), patientInfo, documentBo.getDiagnosticReports());

        return doc.getId();
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
        return document;
    }

}

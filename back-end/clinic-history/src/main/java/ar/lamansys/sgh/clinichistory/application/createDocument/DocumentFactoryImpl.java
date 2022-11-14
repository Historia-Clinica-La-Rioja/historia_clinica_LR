package ar.lamansys.sgh.clinichistory.application.createDocument;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocumentFile.CreateDocumentFile;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAllergies;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDentalActions;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadImmunizations;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedures;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.snvs.application.ports.patient.PatientStorage;

@Service
public class DocumentFactoryImpl implements DocumentFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentFactoryImpl.class);

    private final DocumentService documentService;

    private final CreateDocumentFile createDocumentFile;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final LoadAllergies loadAllergies;

    private final LoadMedications loadMedications;

    private final ClinicalObservationService clinicalObservationService;

    private final LoadImmunizations loadImmunizations;

    private final LoadProcedures loadProcedures;

    private final LoadDiagnosticReports loadDiagnosticReports;

    private final LoadDentalActions loadDentalActions;

	private final FeatureFlagsService featureFlagsService;

	private final PatientStorage patientStorage;

    public DocumentFactoryImpl(DocumentService documentService,
                               CreateDocumentFile createDocumentFile,
                               NoteService noteService,
                               HealthConditionService healthConditionService,
                               LoadAllergies loadAllergies,
                               ClinicalObservationService clinicalObservationService,
                               LoadImmunizations loadImmunizations,
                               LoadProcedures loadProcedures,
                               LoadMedications loadMedications,
                               LoadDiagnosticReports loadDiagnosticReports,
                               LoadDentalActions loadDentalActions,
							   FeatureFlagsService featureFlagsService,
							   PatientStorage patientStorage) {
        this.documentService = documentService;
        this.createDocumentFile = createDocumentFile;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.loadAllergies = loadAllergies;
        this.clinicalObservationService = clinicalObservationService;
        this.loadImmunizations = loadImmunizations;
        this.loadProcedures = loadProcedures;
        this.loadMedications = loadMedications;
        this.loadDiagnosticReports = loadDiagnosticReports;
        this.loadDentalActions = loadDentalActions;
		this.featureFlagsService = featureFlagsService;
		this.patientStorage = patientStorage;
    }

    @Override
	@Transactional
    public Long run(IDocumentBo documentBo, boolean createFile) {

        Document doc = new Document(documentBo.getEncounterId(),
                documentBo.getDocumentStatusId(),
                documentBo.getDocumentType(),
                documentBo.getDocumentSource(),
				documentBo.getInitialDocumentId());
        loadNotes(doc, Optional.ofNullable(documentBo.getNotes()));

		var patientData = patientStorage.getPatientInfo(documentBo.getPatientId()).orElse(null);
		LocalDate patientInternmentAge = documentBo.getPatientInternmentAge();
		if(patientData != null && patientInternmentAge == null)
			doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), LocalDate.now()).toString());
		else if(patientData != null)
			doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), patientInternmentAge).toString());

        doc = documentService.save(doc);

        documentBo.setId(doc.getId());
        PatientInfoBo patientInfo = documentBo.getPatientInfo();
        healthConditionService.loadMainDiagnosis(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getMainDiagnosis()));
        healthConditionService.loadDiagnosis(patientInfo, doc.getId(), documentBo.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientInfo, doc.getId(), documentBo.getPersonalHistories());
        healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), documentBo.getFamilyHistories());
        healthConditionService.loadProblems(patientInfo, doc.getId(), documentBo.getProblems());
        loadAllergies.run(patientInfo, doc.getId(), documentBo.getAllergies());
        loadImmunizations.run(patientInfo, doc.getId(), documentBo.getImmunizations());
        loadMedications.run(patientInfo, doc.getId(), documentBo.getMedications());
        loadProcedures.run(patientInfo, doc.getId(), documentBo.getProcedures());
        loadDentalActions.run(patientInfo, doc.getId(), documentBo.getDentalActions());

        clinicalObservationService.loadRiskFactors(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getRiskFactors()));
        clinicalObservationService.loadAnthropometricData(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getAnthropometricData()));

        loadDiagnosticReports.run(doc.getId(), patientInfo, documentBo.getDiagnosticReports());

        if (createFile)
            generateDocument(documentBo);
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
			document.setIndicationsNoteId(noteService.createNote(notes.getIndicationsNote()));
        });
        return document;
    }

    private void generateDocument(IDocumentBo documentBo) {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(documentBo);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF))
        	createDocumentFile.execute(event);
		else
			createDocumentFile.executeSync(event);
    }

}

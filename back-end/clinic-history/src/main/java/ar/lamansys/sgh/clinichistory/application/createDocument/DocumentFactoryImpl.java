package ar.lamansys.sgh.clinichistory.application.createDocument;

import ar.lamansys.sgh.clinichistory.application.saveDocumentInvolvedProfessionals.SaveDocumentInvolvedProfessionals;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnalgesicTechniques;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticHistory;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticTechniques;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticSubstances;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMeasuringPoints;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadPostAnesthesiaStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedureDescription;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadExternalCause;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadHealthcareProfessionals;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadObstetricEvent;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProsthesis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentFactoryImpl implements DocumentFactory {
    

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

	private final LoadExternalCause loadExternalCause;

	private final LoadObstetricEvent loadObstetricEvent;

	private final LoadHealthcareProfessionals loadHealthcareProfessionals;

	private final LoadProsthesis loadProsthesis;

    private final LoadAnestheticHistory loadAnestheticHistory;
    
    private final LoadAnestheticSubstances loadAnestheticSubstances;

    private final LoadProcedureDescription loadProcedureDescription;

    private final LoadAnalgesicTechniques loadAnalgesicTechniques;

    private final LoadAnestheticTechniques loadAnestheticTechniques;

    private final LoadMeasuringPoints loadMeasuringPoints;

    private final LoadPostAnesthesiaStatus loadPostAnesthesiaStatus;

	private final SaveDocumentInvolvedProfessionals saveDocumentInvolvedProfessionals;

    @Override
	@Transactional
    public Long run(IDocumentBo documentBo, boolean createFile) {

        Document doc = new Document(documentBo.getEncounterId(),
                documentBo.getDocumentStatusId(),
                documentBo.getDocumentType(),
                documentBo.getDocumentSource(),
				documentBo.getPatientId(),
				documentBo.getInstitutionId(),
				documentBo.getInitialDocumentId());
        loadNotes(doc, Optional.ofNullable(documentBo.getNotes()));

		var patientData = patientStorage.getPatientInfo(documentBo.getPatientId()).orElse(null);
		LocalDate patientInternmentAge = documentBo.getPatientInternmentAge();
		if(patientData != null && patientData.getBirthDate() != null && patientInternmentAge == null)
			doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), LocalDate.now()).toString());
		else if(patientData != null && patientData.getBirthDate() != null)
			doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), patientInternmentAge).toString());

		doc.setClinicalSpecialtyId(documentBo.getClinicalSpecialtyId());
        doc = documentService.save(doc);

        documentBo.setId(doc.getId());
        PatientInfoBo patientInfo = documentBo.getPatientInfo();
		Integer patientId = Optional.ofNullable(patientInfo).map(info-> info.getId()).orElse(documentBo.getPatientId());
        healthConditionService.loadMainDiagnosis(patientInfo, doc.getId(), Optional.ofNullable(documentBo.getMainDiagnosis()));
        healthConditionService.loadDiagnosis(patientInfo, doc.getId(), documentBo.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientInfo, doc.getId(), documentBo.getPersonalHistories());
        healthConditionService.loadFamilyHistories(patientInfo, doc.getId(), documentBo.getFamilyHistories());
        healthConditionService.loadProblems(patientInfo, doc.getId(), documentBo.getProblems());
        healthConditionService.loadOtherProblems(patientInfo, doc.getId(), documentBo.getOtherProblems());
		healthConditionService.loadDiagnosis(patientInfo, doc.getId(), documentBo.getPreoperativeDiagnosis());
		healthConditionService.loadDiagnosis(patientInfo, doc.getId(), documentBo.getPostoperativeDiagnosis());
        healthConditionService.loadOtherHistories(patientInfo, doc.getId(), documentBo.getHistories());

		loadAllergies.run(patientInfo, doc.getId(), documentBo.getAllergies());
        loadImmunizations.run(patientId, doc.getId(), documentBo.getImmunizations());
        loadMedications.run(patientId, doc.getId(), documentBo.getMedications());
        loadProcedures.run(patientId, doc.getId(), documentBo.getProcedures());
		loadSurgicalProcedures(patientId, doc.getId(), documentBo);
		loadDentalActions.run(patientInfo, doc.getId(), documentBo.getDentalActions());

        clinicalObservationService.loadRiskFactors(patientId, doc.getId(), Optional.ofNullable(documentBo.getRiskFactors()));
        clinicalObservationService.loadAnthropometricData(patientId, doc.getId(), Optional.ofNullable(documentBo.getAnthropometricData()));

        loadDiagnosticReports.run(doc.getId(), patientId, documentBo.getDiagnosticReports());

		loadExternalCause.run(doc.getId(), Optional.ofNullable(documentBo.getExternalCause()));
		loadObstetricEvent.run(doc.getId(), Optional.ofNullable(documentBo.getObstetricEvent()));
		loadHealthcareProfessionals.run(doc.getId(), documentBo.getHealthcareProfessionals());
		loadProsthesis.run(doc.getId(), documentBo.getProsthesisDescription());

        loadAnestheticHistory.run(doc.getId(), Optional.ofNullable(documentBo.getAnestheticHistory()));
        loadAnestheticSubstances.run(doc.getId(), documentBo.getPreMedications());
        loadAnestheticSubstances.run(doc.getId(), documentBo.getAnestheticPlans());
        loadProcedureDescription.run(doc.getId(), Optional.ofNullable(documentBo.getProcedureDescription()));
        loadAnalgesicTechniques.run(doc.getId(), documentBo.getAnalgesicTechniques());
        loadAnestheticTechniques.run(doc.getId(), documentBo.getAnestheticTechniques());
        loadAnestheticSubstances.run(doc.getId(), documentBo.getFluidAdministrations());
        loadAnestheticSubstances.run(doc.getId(), documentBo.getAnestheticAgents());
        loadAnestheticSubstances.run(doc.getId(), documentBo.getNonAnestheticDrugs());
        loadAnestheticSubstances.run(doc.getId(), documentBo.getAntibioticProphylaxis());
        loadMeasuringPoints.run(doc.getId(), documentBo.getMeasuringPoints());
        loadPostAnesthesiaStatus.run(doc.getId(), Optional.ofNullable(documentBo.getPostAnesthesiaStatus()));

		saveDocumentInvolvedProfessionals.run(doc.getId(), documentBo.getInvolvedHealthcareProfessionalIds());

        if (createFile)
            generateDocument(documentBo);
        return doc.getId();
    }

    private Document loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
        log.debug("Input parameters -> anamnesisDocument {}, notes {}", document, optNotes);
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

	private void loadSurgicalProcedures(Integer patientId, Long documentId, IDocumentBo documentBo){
		loadProcedures.run(patientId,documentId, documentBo.getSurgeryProcedures());
		loadProcedures.run(patientId,documentId, documentBo.getAnesthesia());
		loadProcedures.run(patientId, documentId, documentBo.getCultures());
		loadProcedures.run(patientId, documentId, documentBo.getFrozenSectionBiopsies());
		loadProcedures.run(patientId, documentId, documentBo.getDrainages());
	}

    private void generateDocument(IDocumentBo documentBo) {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(documentBo);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF))
        	createDocumentFile.execute(event);
		else
			createDocumentFile.executeSync(event);
    }

}

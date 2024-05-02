package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentDownloadDataBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EReferableConcept;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnalgesicTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentHealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.NewbornBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAnestheticHistoryRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAnestheticTechniqueRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentDiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentExternalCauseRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthcareProfessionalRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMeasuringPointRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentObstetricEventRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAnestheticSubstanceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentPostAnesthesiaStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureDescriptionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProsthesisRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentReferableConceptRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentReportSnomedConceptRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentTriageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDownloadDataVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCause;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthcareProfessional;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMeasuringPoint;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticSubstance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProsthesis;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConcept;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConceptPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentTriage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.AllergyConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ExternalCauseVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObstetricEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OtherRiskFactorVo;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";
    
    private static final String LOGGING_DOCUMENT_ID = "Input parameters -> documentId {}";
    
    private static final String LOGGING_DELETE_SUCCESS = "Delete success";

	private static final String LOGGING_UPDATE_MODIFICATION_REASON_SUCCESS = "Update modificaton reason success";

    private final DocumentRepository documentRepository;

    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    private final DocumentImmunizationRepository documentImmunizationRepository;

    private final DocumentProcedureRepository documentProcedureRepository;

    private final DocumentRiskFactorRepository documentRiskFactorRepository;

    private final DocumentLabRepository documentLabRepository;

    private final DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;

    private final DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

    private final DocumentDiagnosticReportRepository documentDiagnosticReportRepository;

    private final DocumentOdontologyProcedureRepository documentOdontologyProcedureRepository;

    private final DocumentOdontologyDiagnosticRepository documentOdontologyDiagnosticRepository;

	private final DocumentExternalCauseRepository documentExternalCauseRepository;

	private final DocumentObstetricEventRepository documentObstetricEventRepository;
	
	private final DocumentTriageRepository documentTriageRepository;

    private final DocumentReportSnomedConceptRepository documentReportSnomedConceptRepository;

    private final SnomedService snomedService;

	private final DocumentHealthcareProfessionalRepository documentHealthcareProfessionalRepository;

	private final DocumentProsthesisRepository documentProsthesisRepository;

    private final DocumentAnestheticHistoryRepository documentAnestheticHistoryRepository;
    
    private final DocumentAnestheticSubstanceRepository documentAnestheticSubstanceRepository;

    private final DocumentProcedureDescriptionRepository documentProcedureDescriptionRepository;

    private final DocumentAnestheticTechniqueRepository documentAnestheticTechniqueRepository;

    private final DocumentMeasuringPointRepository documentMeasuringPointRepository;

    private final DocumentPostAnesthesiaStatusRepository documentPostAnesthesiaStatusRepository;

	private final DocumentReferableConceptRepository documentReferableConceptRepository;

    @Override
    public Optional<Document> findById(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        Optional<Document> result = documentRepository.findById(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Document save(Document document) {
        log.debug("Input parameters -> document {}", document);
        document = documentRepository.save(document);
        log.debug(OUTPUT, document);
        return document;
    }

    @Override
    public DocumentHealthCondition createDocumentHealthCondition(Long documentId, Integer healthConditionId) {
        log.debug("Input parameters -> documentId {}, healthConditionId {}", documentId, healthConditionId);
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        result = documentHealthConditionRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentRiskFactor createDocumentRiskFactor(Long documentId, Integer observationRiskFactorsId) {
        log.debug("Input parameters -> documentId {}, observationRiskFactorsId {}", documentId, observationRiskFactorsId);
        DocumentRiskFactor result = new DocumentRiskFactor(documentId, observationRiskFactorsId);
        result = documentRiskFactorRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentLab createDocumentLab(Long documentId, Integer observationLabId) {
        log.debug("Input parameters -> documentId {}, observationLabId {}", documentId, observationLabId);
        DocumentLab result = new DocumentLab(documentId, observationLabId);
        result = documentLabRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentAllergyIntolerance createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId) {
        log.debug("Input parameters -> documentId {}, allergyIntoleranceId {}", documentId, allergyIntoleranceId);
        DocumentAllergyIntolerance result = new DocumentAllergyIntolerance(documentId, allergyIntoleranceId);
        result = documentAllergyIntoleranceRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentInmunization createImmunization(Long documentId, Integer immunizationId) {
        log.debug("Input parameters -> documentId {}, immunizationId {}", documentId, immunizationId);
        DocumentInmunization result = new DocumentInmunization(documentId, immunizationId);
        result = documentImmunizationRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentMedicamentionStatement createDocumentMedication(Long documentId, Integer medicationStatementId) {
        log.debug("Input parameters -> documentId {}, medicationStatementId {}", documentId, medicationStatementId);
        DocumentMedicamentionStatement result = new DocumentMedicamentionStatement(documentId, medicationStatementId);
        result = documentMedicamentionStatementRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentProcedure createDocumentProcedure(Long documentId, Integer procedureId) {
        log.debug("Input parameters -> documentId {}, procedureId {}", documentId, procedureId);
        DocumentProcedure result = new DocumentProcedure(documentId, procedureId);
        result = documentProcedureRepository.save(result);
        log.debug(OUTPUT, result);
        return result;

    }

    @Override
    public DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId) {
        log.debug("Input parameters -> documentId {}, procedureId {}", documentId, diagnosticReportId);
        DocumentDiagnosticReport result = new DocumentDiagnosticReport(documentId, diagnosticReportId);
        result = documentDiagnosticReportRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentOdontologyProcedure createDocumentOdontologyProcedure(Long documentId, Integer odontologyProcedureId) {
        log.debug("Input parameters -> documentId {}, odontologyProcedureId {}", documentId, odontologyProcedureId);
        DocumentOdontologyProcedure result = new DocumentOdontologyProcedure(documentId, odontologyProcedureId);
        result = documentOdontologyProcedureRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentOdontologyDiagnostic createDocumentOdontologyDiagnostic(Long documentId, Integer odontologyDiagnosticId) {
        log.debug("Input parameters -> documentId {}, odontologyDiagnosticId {}", documentId, odontologyDiagnosticId);
        DocumentOdontologyDiagnostic result = new DocumentOdontologyDiagnostic(documentId, odontologyDiagnosticId);
        result = documentOdontologyDiagnosticRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(resultQuery);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ImmunizationBo> getImmunizationStateFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ImmunizationVo> resultQuery = documentImmunizationRepository.getImmunizationStateFromDocument(documentId);
        List<ImmunizationBo> result = resultQuery.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public ReferableItemBo<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		ReferableItemBo<AllergyConditionBo> result = getAllergyConditionBoReferableItemBo(documentId);
		log.debug(OUTPUT, result);
        return result;
    }

	private ReferableItemBo<AllergyConditionBo> getAllergyConditionBoReferableItemBo(Long documentId) {
		List<AllergyConditionVo> resultQuery = documentAllergyIntoleranceRepository.getAllergyIntoleranceStateFromDocument(documentId);
		List<AllergyConditionBo> allergies = resultQuery.stream().map(AllergyConditionBo::new).collect(Collectors.toList());
		Boolean isReferred = documentReferableConceptRepository.isReferredIdByDocumentAndConceptId(documentId, EReferableConcept.ALLERGY.getId());
		return new ReferableItemBo<>(allergies, isReferred);
	}

	@Override
    public List<MedicationBo> getMedicationStateFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        List<MedicationVo> resultQuery = documentMedicamentionStatementRepository.getMedicationStateFromDocument(documentId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ClinicalObservationVo> clinicalObservationVos = documentRiskFactorRepository.getRiskFactorStateFromDocument(documentId);
        clinicalObservationVos.addAll(documentLabRepository.getLabStateFromDocument(documentId));
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(clinicalObservationVos);
        AnthropometricDataBo result = resultQuery.getLastAnthropometricData().orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public RiskFactorBo getRiskFactorStateFromDocument(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(documentRiskFactorRepository.getRiskFactorStateFromDocument(documentId));
        RiskFactorBo result = resultQuery.getLastRiskFactors().orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }

	@Override
	public List<ProcedureBo> getProcedureStateFromDocument(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		List<ProcedureVo> resultQuery = documentProcedureRepository.getProcedureStateFromDocument(documentId);
		List<ProcedureBo> result = resultQuery.stream().map(ProcedureBo::new).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
    public List<Updateable> getUpdatableDocuments(Integer internmentEpisodeId) {
        return documentRepository.getUpdatablesDocuments(internmentEpisodeId);
    }

    @Override
    public DocumentMedicamentionStatement getDocumentFromMedication(Integer mid) {
        log.debug("medicationId -> {}", mid);
        DocumentMedicamentionStatement result = documentMedicamentionStatementRepository.findByMedicationId(mid);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentDiagnosticReport getDocumentFromDiagnosticReport(Integer drid) {
        log.debug("DiagnosticReportId -> {}", drid);
        DocumentDiagnosticReport result = documentDiagnosticReportRepository.findByDiagnosticReportId(drid);
        log.debug(OUTPUT, result);
        return result;
    }

	@Override
	public List<DocumentHealthcareProfessionalBo> getHealthcareProfessionalsFromDocument(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		List<DocumentHealthcareProfessionalBo> result = documentHealthcareProfessionalRepository.getFromDocument(documentId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<String> getProsthesisDescriptionFromDocument(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		return documentProsthesisRepository.findById(documentId).map(DocumentProsthesis::getDescription);
	}

	@Override
    public List<Long> getDocumentId(Integer sourceId, Short sourceTypeId) {
        return documentRepository.findBySourceIdAndSourceTypeId(sourceId,sourceTypeId);
    }

    @Override
    public List<Long> getDocumentIdBySourceAndType(Integer sourceId, Short typeId) {
        return documentRepository.findBySourceIdAndTypeId(sourceId, typeId);
    }

    @Override
    public void deleteHealthConditionHistory(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteAllergiesHistory(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteImmunizationsHistory(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteMedicationsHistory(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteObservationsRiskFactorsHistory(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteObservationsLabHistory(Long documentId) {
        log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void deleteProceduresHistory(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		log.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void deleteById(Long documentId, String documentStatus) {
		log.debug("Input parameters -> documentId {}, documentStatus {}", documentId, documentStatus);
		documentRepository.findById(documentId).ifPresentOrElse((document) -> {
			document.setStatusId(documentStatus);
			documentRepository.save(document);
			documentRepository.deleteById(documentId);
		},() -> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
		log.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void updateDocumentModificationReason(Long documentId, String reason) {
		log.debug("Input parameters -> documentId {}, reason {}", documentId, reason);
		documentRepository.findById(documentId).ifPresentOrElse((document) -> {
			document.setModificationReason(reason);
			documentRepository.save(document);
			},() -> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
		log.debug(LOGGING_UPDATE_MODIFICATION_REASON_SUCCESS);
	}

	@Override
	public Short getSourceType(Long documentId) {
		log.debug("Input parameters -> documentId {}", documentId);
		Short result = this.documentRepository.getSourceTypeId(documentId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<DentalActionBo> getDentalActionsFromDocument(Long documentId) {
		log.debug(LOGGING_DOCUMENT_ID, documentId);

		List<DentalActionBo> resultOdontologyProcedure = documentOdontologyProcedureRepository
				.getOdontologyProcedureFromDocument(documentId)
				.stream()
				.map(this::mapToOdontologyProcedure)
				.collect(Collectors.toList());
		List<DentalActionBo> resultOdontologyDiagnostic = documentOdontologyDiagnosticRepository
				.getOdontologyDiagnosticFromDocument(documentId)
				.stream()
				.map(this::mapToOdontologyDiagnostic)
				.collect(Collectors.toList());
		resultOdontologyProcedure.addAll(resultOdontologyDiagnostic);
		log.debug(OUTPUT, resultOdontologyProcedure);
		return resultOdontologyProcedure;

	}

	@Override
	public DocumentExternalCause createDocumentExternalCause(Long documentId, Integer externalCauseId) {
		log.debug("Input parameters -> documentId {}, externalCauseId {}", documentId, externalCauseId);
		DocumentExternalCause result = new DocumentExternalCause(documentId, externalCauseId);
		result = documentExternalCauseRepository.save(result);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public ExternalCauseBo getExternalCauseFromDocument(Long documentId){
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		ExternalCauseVo externalCauseVo = documentExternalCauseRepository.getExternalCauseFromDocument(documentId);
		if (externalCauseVo == null)
			return null;
		ExternalCauseBo result = new ExternalCauseBo(externalCauseVo);
		log.debug(OUTPUT, result);
        return result;
	}
    
	@Override
    public List<Long> getDocumentsIdsFromPatient(Integer patient) {
		log.debug("Input parameters -> patientId {}", patient);
		List<Long> result = documentRepository.getIdsByPatientId(patient);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public DocumentObstetricEvent createDocumentObstetricEvent(Long documentId, Integer obstetricEventId) {
		log.debug("Input parameters -> documentId {}, externalCauseId {}", documentId, obstetricEventId);
		DocumentObstetricEvent result = new DocumentObstetricEvent(documentId, obstetricEventId);
		result = documentObstetricEventRepository.save(result);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public ObstetricEventBo getObstetricEventFromDocument(Long documentId){
		log.debug(LOGGING_DOCUMENT_ID, documentId);
		ObstetricEvent obstetricEvent = documentObstetricEventRepository.getObstetricEventFromDocument(documentId);
		if (obstetricEvent == null)
			return null;
		List<NewbornBo> newborns = documentObstetricEventRepository.getObstetricEventNewbornsFromDocument(documentId).stream().map(NewbornBo::new).collect(Collectors.toList());
		ObstetricEventBo result = new ObstetricEventBo(obstetricEvent);
		result.setNewborns(newborns);
		log.debug(OUTPUT, result);
		return result;
	}
	
	@Override
	public OtherRiskFactorBo getOtherRiskFactors(Long id) {
		log.debug("Input parameters -> documentId {}", id);
		OtherRiskFactorBo result = mapToOtherRiskFactorBo(documentRiskFactorRepository.getOtherRiskFactorsFromDocument(id));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public DocumentDownloadDataBo getDocumentDownloadDataByTriage(Integer triageId) {
		log.debug("Input parameters -> triageId {}", triageId);
		DocumentDownloadDataBo result = mapToDocumentDownloadDataBo(documentRepository.getDocumentIdByTriageId(triageId));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public DocumentDownloadDataBo getDocumentDownloadDataByAppointmentId(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		DocumentDownloadDataBo result = mapToDocumentDownloadDataBo(documentRepository.getDocumentIdByAppointmentId(appointmentId));
		log.debug("Output -> {}", result);
		return result;
	}

	private DocumentDownloadDataBo mapToDocumentDownloadDataBo(DocumentDownloadDataVo documentDownloadData) {
		DocumentDownloadDataBo result = new DocumentDownloadDataBo();
		if (documentDownloadData != null) {
			result.setId(documentDownloadData.getId());
			result.setFileName(documentDownloadData.getFileName());
		}
		return result;
	}

	@Override
	public DocumentTriage createDocumentTriage(Long documentId, Integer triageId) {
		log.debug("Input parameters -> documentId {}, triageId {}", documentId, triageId);
		DocumentTriage result = documentTriageRepository.save(new DocumentTriage(documentId, triageId));
		log.debug("Output -> {}", result);
		return result;
	}

    @Override
    public List<ConclusionBo> getConclusionsFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        List<ConclusionBo> result = documentReportSnomedConceptRepository.getSnomedConceptsByReportDocumentId(documentId)
                .stream()
                .map(snomedService::getSnomed)
                .map(ConclusionBo::new)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

	@Override
	public DocumentHealthcareProfessional createDocumentHealthcareProfessional(Long documentId, DocumentHealthcareProfessionalBo professional) {
		log.debug("Input parameters -> documentId {}, professional {}", documentId, professional);
		DocumentHealthcareProfessional result = documentHealthcareProfessionalRepository.save(new DocumentHealthcareProfessional(professional.getId(), documentId, professional.getHealthcareProfessional().getId(), professional.getType().getId(), professional.getComments()));
		log.debug("Output -> {}", result);
		return result;	}

	@Override
	public DocumentProsthesis createDocumentProsthesis(Long documentId, String description) {
		log.debug("Input parameters -> documentId {}, prosthesisDescription {}", documentId, description);
		DocumentProsthesis result = documentProsthesisRepository.save(new DocumentProsthesis(documentId, description));
		log.debug("Output -> {}", result);
		return result;
	}

    @Override
    public DocumentAnestheticSubstance createDocumentAnestheticSubstance(Long documentId, Integer substanceId) {
        log.debug("Input parameters -> documentId {}, substanceId {}", documentId, substanceId);
        DocumentAnestheticSubstance result = new DocumentAnestheticSubstance(documentId, substanceId);
        result = documentAnestheticSubstanceRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AnestheticSubstanceBo> getAnestheticSubstancesStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        List<AnestheticSubstanceBo> result = documentAnestheticSubstanceRepository.getAnestheticSubstancesStateFromDocument(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public AnestheticHistoryBo getAnestheticHistoryStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        AnestheticHistoryBo result = documentAnestheticHistoryRepository.findById(documentId)
                .map((documentAnestheticHistory) -> new AnestheticHistoryBo(documentAnestheticHistory.getDocumentId(), documentAnestheticHistory.getStateId(), documentAnestheticHistory.getZoneId()))
                .orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public ProcedureDescriptionBo getProcedureDescriptionStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        ProcedureDescriptionBo result = documentProcedureDescriptionRepository.getDocumentProcedureDescription(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AnalgesicTechniqueBo> getAnalgesicTechniquesStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        List<AnalgesicTechniqueBo> result = documentAnestheticSubstanceRepository.getAnalgesicTechniquesStateFromDocument(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentAnestheticTechnique createDocumentAnestheticTechnique(Long documentId, Integer anestheticTechniqueId) {
        log.debug("Input parameters -> documentId {}, anestheticTechniqueId {}", documentId, anestheticTechniqueId);
        DocumentAnestheticTechnique result = new DocumentAnestheticTechnique(documentId, anestheticTechniqueId);
        result = documentAnestheticTechniqueRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AnestheticTechniqueBo> getAnestheticTechniquesStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        List<AnestheticTechniqueBo> result = documentAnestheticTechniqueRepository.getAnestheticTechniquesStateFromDocument(documentId);
        result.forEach(anestheticTechniqueBo -> anestheticTechniqueBo.setTrachealIntubationMethodIds(
                documentAnestheticTechniqueRepository.getTrachealIntubationState(anestheticTechniqueBo.getId())));
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentMeasuringPoint createDocumentMeasuringPoint(Long documentId, Integer measuringPointId) {
        log.debug("Input parameters -> documentId {}, measuringPointId {}", documentId, measuringPointId);
        DocumentMeasuringPoint result = new DocumentMeasuringPoint(documentId, measuringPointId);
        result = documentMeasuringPointRepository.save(result);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<MeasuringPointBo> getMeasuringPointStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        List<MeasuringPointBo> result = documentMeasuringPointRepository.getMeasuringPointStateFromDocument(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public PostAnesthesiaStatusBo getPostAnesthesiaStatusStateFromDocument(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        PostAnesthesiaStatusBo result = documentPostAnesthesiaStatusRepository.getDocumentPostAnesthesiaStatus(documentId);
        log.debug(OUTPUT, result);
        return result;
    }

	@Override
	public void createDocumentRefersAllergy(Long documentId, Boolean refersAllergy) {
		log.debug("Input parameters -> documentId {}, refersAllergy {}", documentId, refersAllergy);
		if (refersAllergy != null)
			documentReferableConceptRepository.save(parseDocumentReferableConcept(documentId, EReferableConcept.ALLERGY.getId(), refersAllergy));
		log.debug("Output -> Value saved successfully");
	}

	private DocumentReferableConcept parseDocumentReferableConcept(Long documentId, Short referableConceptId, boolean isReferred) {
		DocumentReferableConceptPK resultPK = new DocumentReferableConceptPK(documentId, referableConceptId);
		return new DocumentReferableConcept(resultPK, isReferred);
	}

	private DentalActionBo mapToOdontologyProcedure(Object[] row) {
		var result = new DentalActionBo();
		result.setDiagnostic(false);
		result.setSnomed(new SnomedBo((Snomed)row[1]));
		result.setTooth(new SnomedBo((Snomed)row[2]));
		result.setSurface(new SnomedBo((Snomed)row[3]));
		return result;
	}

	private DentalActionBo mapToOdontologyDiagnostic(Object[] row) {
		var result = new DentalActionBo();
		result.setDiagnostic(true);
		result.setSnomed(new SnomedBo((Snomed)row[1]));
		result.setTooth(new SnomedBo((Snomed)row[2]));
		result.setSurface(new SnomedBo((Snomed)row[3]));
		return result;
	}

	private OtherRiskFactorBo mapToOtherRiskFactorBo(OtherRiskFactorVo otherRiskFactors) {
		if (otherRiskFactors != null)
			return new OtherRiskFactorBo(otherRiskFactors);
		return new OtherRiskFactorBo();
	}

}

package ar.lamansys.sgh.clinichistory.application.document;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentDiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentHealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.AllergyConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

@Service
public class  DocumentServiceImpl implements DocumentService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);
    
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

	public DocumentServiceImpl(DocumentRepository documentRepository,
                               DocumentHealthConditionRepository documentHealthConditionRepository,
                               DocumentImmunizationRepository documentImmunizationRepository,
                               DocumentProcedureRepository documentProcedureRepository,
                               DocumentRiskFactorRepository documentRiskFactorRepository,
                               DocumentLabRepository documentLabRepository,
                               DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository,
                               DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository,
                               DocumentDiagnosticReportRepository documentDiagnosticReportRepository,
                               DocumentOdontologyProcedureRepository documentOdontologyProcedureRepository,
							   DocumentOdontologyDiagnosticRepository documentOdontologyDiagnosticRepository) {
        this.documentRepository = documentRepository;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
        this.documentImmunizationRepository = documentImmunizationRepository;
        this.documentProcedureRepository = documentProcedureRepository;
        this.documentRiskFactorRepository = documentRiskFactorRepository;
        this.documentLabRepository = documentLabRepository;
        this.documentAllergyIntoleranceRepository = documentAllergyIntoleranceRepository;
        this.documentMedicamentionStatementRepository = documentMedicamentionStatementRepository;
        this.documentDiagnosticReportRepository = documentDiagnosticReportRepository;
        this.documentOdontologyProcedureRepository = documentOdontologyProcedureRepository;
        this.documentOdontologyDiagnosticRepository = documentOdontologyDiagnosticRepository;
    }

    @Override
    public Optional<Document> findById(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        Optional<Document> result = documentRepository.findById(documentId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Document save(Document document) {
        LOG.debug("Input parameters -> document {}", document);
        document = documentRepository.save(document);
        LOG.debug(OUTPUT, document);
        return document;
    }

    @Override
    public DocumentHealthCondition createDocumentHealthCondition(Long documentId, Integer healthConditionId) {
        LOG.debug("Input parameters -> documentId {}, healthConditionId {}", documentId, healthConditionId);
        DocumentHealthCondition result = new DocumentHealthCondition(documentId, healthConditionId);
        result = documentHealthConditionRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentRiskFactor createDocumentRiskFactor(Long documentId, Integer observationRiskFactorsId) {
        LOG.debug("Input parameters -> documentId {}, observationRiskFactorsId {}", documentId, observationRiskFactorsId);
        DocumentRiskFactor result = new DocumentRiskFactor(documentId, observationRiskFactorsId);
        result = documentRiskFactorRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentLab createDocumentLab(Long documentId, Integer observationLabId) {
        LOG.debug("Input parameters -> documentId {}, observationLabId {}", documentId, observationLabId);
        DocumentLab result = new DocumentLab(documentId, observationLabId);
        result = documentLabRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentAllergyIntolerance createDocumentAllergyIntolerance(Long documentId, Integer allergyIntoleranceId) {
        LOG.debug("Input parameters -> documentId {}, allergyIntoleranceId {}", documentId, allergyIntoleranceId);
        DocumentAllergyIntolerance result = new DocumentAllergyIntolerance(documentId, allergyIntoleranceId);
        result = documentAllergyIntoleranceRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentInmunization createImmunization(Long documentId, Integer immunizationId) {
        LOG.debug("Input parameters -> documentId {}, immunizationId {}", documentId, immunizationId);
        DocumentInmunization result = new DocumentInmunization(documentId, immunizationId);
        result = documentImmunizationRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentMedicamentionStatement createDocumentMedication(Long documentId, Integer medicationStatementId) {
        LOG.debug("Input parameters -> documentId {}, medicationStatementId {}", documentId, medicationStatementId);
        DocumentMedicamentionStatement result = new DocumentMedicamentionStatement(documentId, medicationStatementId);
        result = documentMedicamentionStatementRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentProcedure createDocumentProcedure(Long documentId, Integer procedureId) {
        LOG.debug("Input parameters -> documentId {}, procedureId {}", documentId, procedureId);
        DocumentProcedure result = new DocumentProcedure(documentId, procedureId);
        result = documentProcedureRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;

    }

    @Override
    public DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId) {
        LOG.debug("Input parameters -> documentId {}, procedureId {}", documentId, diagnosticReportId);
        DocumentDiagnosticReport result = new DocumentDiagnosticReport(documentId, diagnosticReportId);
        result = documentDiagnosticReportRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentOdontologyProcedure createDocumentOdontologyProcedure(Long documentId, Integer odontologyProcedureId) {
        LOG.debug("Input parameters -> documentId {}, odontologyProcedureId {}", documentId, odontologyProcedureId);
        DocumentOdontologyProcedure result = new DocumentOdontologyProcedure(documentId, odontologyProcedureId);
        result = documentOdontologyProcedureRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentOdontologyDiagnostic createDocumentOdontologyDiagnostic(Long documentId, Integer odontologyDiagnosticId) {
        LOG.debug("Input parameters -> documentId {}, odontologyDiagnosticId {}", documentId, odontologyDiagnosticId);
        DocumentOdontologyDiagnostic result = new DocumentOdontologyDiagnostic(documentId, odontologyDiagnosticId);
        result = documentOdontologyDiagnosticRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public GeneralHealthConditionBo getHealthConditionFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(resultQuery);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ImmunizationBo> getImmunizationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ImmunizationVo> resultQuery = documentImmunizationRepository.getImmunizationStateFromDocument(documentId);
        List<ImmunizationBo> result = resultQuery.stream().map(ImmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<AllergyConditionBo> getAllergyIntoleranceStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<AllergyConditionVo> resultQuery = documentAllergyIntoleranceRepository.getAllergyIntoleranceStateFromDocument(documentId);
        List<AllergyConditionBo> result = resultQuery.stream().map(AllergyConditionBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<MedicationBo> getMedicationStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<MedicationVo> resultQuery = documentMedicamentionStatementRepository.getMedicationStateFromDocument(documentId);
        List<MedicationBo> result = resultQuery.stream().map(MedicationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public AnthropometricDataBo getAnthropometricDataStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        List<ClinicalObservationVo> clinicalObservationVos = documentRiskFactorRepository.getRiskFactorStateFromDocument(documentId);
        clinicalObservationVos.addAll(documentLabRepository.getLabStateFromDocument(documentId));
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(clinicalObservationVos);
        AnthropometricDataBo result = resultQuery.getLastAnthropometricData().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public RiskFactorBo getRiskFactorStateFromDocument(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
        MapClinicalObservationVo resultQuery = new MapClinicalObservationVo(documentRiskFactorRepository.getRiskFactorStateFromDocument(documentId));
        RiskFactorBo result = resultQuery.getLastRiskFactors().orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public List<ProcedureBo> getProcedureStateFromDocument(Long documentId) {
		LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		List<ProcedureVo> resultQuery = documentProcedureRepository.getProcedureStateFromDocument(documentId);
		List<ProcedureBo> result = resultQuery.stream().map(ProcedureBo::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
    public List<Updateable> getUpdatableDocuments(Integer internmentEpisodeId) {
        return documentRepository.getUpdatablesDocuments(internmentEpisodeId);
    }

    @Override
    public DocumentMedicamentionStatement getDocumentFromMedication(Integer mid) {
        LOG.debug("medicationId -> {}", mid);
        DocumentMedicamentionStatement result = documentMedicamentionStatementRepository.findByMedicationId(mid);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public DocumentDiagnosticReport getDocumentFromDiagnosticReport(Integer drid) {
        LOG.debug("DiagnosticReportId -> {}", drid);
        DocumentDiagnosticReport result = documentDiagnosticReportRepository.findByDiagnosticReportId(drid);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<Long> getDocumentId(Integer sourceId, Short sourceTypeId) {
        return documentRepository.findBySourceIdAndSourceTypeId(sourceId,sourceTypeId);
    }

    @Override
    public void deleteHealthConditionHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteAllergiesHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteImmunizationsHistory(Long documentId) {
		LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteMedicationsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteObservationsRiskFactorsHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

    @Override
    public void deleteObservationsLabHistory(Long documentId) {
        LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void deleteProceduresHistory(Long documentId) {
		LOG.debug(LOGGING_DOCUMENT_ID, documentId);
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void deleteById(Long documentId, String documentStatus) {
		LOG.debug("Input parameters -> documentId {}, documentStatus {}", documentId, documentStatus);
		documentRepository.findById(documentId).ifPresentOrElse((document) -> {
			document.setStatusId(documentStatus);
			documentRepository.save(document);
			documentRepository.deleteById(documentId);
		},() -> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
		LOG.debug(LOGGING_DELETE_SUCCESS);
	}

	@Override
	public void updateDocumentModificationReason(Long documentId, String reason) {
		LOG.debug("Input parameters -> documentId {}, reason {}", documentId, reason);
		documentRepository.findById(documentId).ifPresentOrElse((document) -> {
			document.setModificationReason(reason);
			documentRepository.save(document);
			},() -> new NotFoundException("document-not-exists", String.format("No existe el documento con id %s", documentId)));
		LOG.debug(LOGGING_UPDATE_MODIFICATION_REASON_SUCCESS);
	}

	@Override
	public Short getSourceType(Long documentId) {
		LOG.debug("Input parameters -> documentId {}", documentId);
		Short result = this.documentRepository.getSourceTypeId(documentId);
		LOG.debug(OUTPUT, result);
		return result;
	}

}

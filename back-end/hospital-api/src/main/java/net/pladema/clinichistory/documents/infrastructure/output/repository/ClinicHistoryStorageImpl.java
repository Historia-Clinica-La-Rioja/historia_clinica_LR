package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.CHCounterReferenceBo;
import net.pladema.clinichistory.documents.domain.CHDocumentBo;
import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;

import net.pladema.clinichistory.documents.domain.CHMedicationRequestBo;
import net.pladema.clinichistory.documents.domain.CHNursingConsultationBo;
import net.pladema.clinichistory.documents.domain.CHOdontologyBo;
import net.pladema.clinichistory.documents.domain.CHOutpatientBo;
import net.pladema.clinichistory.documents.domain.CHServiceRequestBo;
import net.pladema.clinichistory.documents.domain.ECHDocumentType;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;

import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.HistoricClinicHistoryDownload;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicHistoryStorageImpl implements ClinicHistoryStorage {

	private final VClinicHistoryRepository repository;
	private final EntityManager entityManager;
	private final SharedStaffPort sharedStaffPort;
	private final FeatureFlagsService featureFlagsService;
	private final HistoricClinicHistoryDownloadRepository historicClinicHistoryDownloadRepository;

	public ClinicHistoryStorageImpl(VClinicHistoryRepository vClinicHistoryRepository,
									EntityManager entityManager,
									SharedStaffPort sharedStaffPort,
									FeatureFlagsService featureFlagsService,
									HistoricClinicHistoryDownloadRepository historicClinicHistoryDownloadRepository)
	{
		this.repository = vClinicHistoryRepository;
		this.entityManager = entityManager;
		this.sharedStaffPort = sharedStaffPort;
		this.featureFlagsService = featureFlagsService;
		this.historicClinicHistoryDownloadRepository = historicClinicHistoryDownloadRepository;
	}

	@Override
	public List<CHDocumentSummaryBo> getPatientClinicHistory(Integer patientId, LocalDate from, LocalDate to) {

		List<VClinicHistory> resultList = repository.getPatientClinicHistory(patientId, LocalDateTime.of(from, LocalTime.MIN), LocalDateTime.of(to, LocalTime.MAX));

		return  resultList
				.stream()
				.map(this::mapToSummaryBo)
				.collect(Collectors.toList());
	}

	@Override
	public List<CHDocumentBo> getClinicHistoryDocuments(List<Long> ids) {
		return repository.findAllById(ids)
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());
	}

	@Override
	public Integer savePatientClinicHistoryLastPrint(Integer userId, Integer patientId, Integer institutionId){
		return historicClinicHistoryDownloadRepository
				.save(new HistoricClinicHistoryDownload(null, userId, patientId, LocalDateTime.now(), institutionId)).getId();
	}

	private CHDocumentBo mapToBo(VClinicHistory row){
		ECHEncounterType encounterType = getEncounterType(row);
		ECHDocumentType documentType = getDocumentType(row);
		if (row.getDocumentTypeId().equals(EDocumentType.ORDER.getId())) return new CHServiceRequestBo(row, encounterType, documentType);
		if (row.getDocumentTypeId().equals(EDocumentType.OUTPATIENT.getId())) return new CHOutpatientBo(row, encounterType, documentType);
		if (row.getDocumentTypeId().equals(EDocumentType.ODONTOLOGY.getId())) return new CHOdontologyBo(row, encounterType, documentType);
		if (row.getDocumentTypeId().equals(EDocumentType.COUNTER_REFERENCE.getId())) return new CHCounterReferenceBo(row, encounterType, documentType);
		if (row.getDocumentTypeId().equals(EDocumentType.RECIPE.getId())) return new CHMedicationRequestBo(row, encounterType, documentType);
		if (row.getDocumentTypeId().equals(EDocumentType.NURSING.getId())) return new CHNursingConsultationBo(row, encounterType, documentType);
		return null;
	}
	private CHDocumentSummaryBo mapToSummaryBo(VClinicHistory row){
		CHDocumentSummaryBo result = new CHDocumentSummaryBo();
		var professional = sharedStaffPort.getProfessionalComplete(row.getCreatedBy());
		String professionalCompleteName =
				(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && professional.getNameSelfDetermination()!= null ? professional.getNameSelfDetermination() : professional.getFirstName()) + ' ' + professional.getLastName();
		result.setId(row.getId());
		result.setInstitution(row.getInstitution());
		result.setProfessional(professionalCompleteName);
		result.setStartDate(row.getStartDate() != null ? row.getStartDate() : row.getCreatedOn());
		result.setEndDate(row.getEndDate() != null ? row.getEndDate() : row.getCreatedOn());
		result.setEncounterType(getEncounterType(row));
		result.setDocumentType(getDocumentType(row));
		result.setProblems(mapProblems(row.getHealthConditionSummary().getProblems()));
		return result;
	}

	private String mapProblems(String problems){
		if(problems.isBlank()) return problems;
		int startIndex = problems.contains("Principal:") ? problems.indexOf("Principal:") + 11 : (problems.contains("Otro:") ? problems.indexOf("Otro:") + 6 : problems.indexOf("Problemas:") + 11);
		int endIndex = problems.contains("|") ? problems.indexOf("|") : problems.length();
		return problems.substring(startIndex, endIndex);
	}

	private ECHEncounterType getEncounterType (VClinicHistory row){
		if(row.getSourceTypeId().equals(ESourceType.HOSPITALIZATION.getId())
			|| (row.getSourceTypeId().equals(ESourceType.ORDER.getId()) && (row.getRequestSourceTypeId().equals(ESourceType.HOSPITALIZATION.getId()))))
			return ECHEncounterType.HOSPITALIZATION;
		if(row.getSourceTypeId().equals(ESourceType.EMERGENCY_CARE.getId()))
			return ECHEncounterType.EMERGENCY_CARE;
		return ECHEncounterType.OUTPATIENT;
	}

	private ECHDocumentType getDocumentType(VClinicHistory row){
		if (row.getDocumentTypeId().equals(EDocumentType.EPICRISIS.getId()))
			return ECHDocumentType.EPICRISIS;
		if (row.getDocumentTypeId().equals(EDocumentType.RECIPE.getId()) || row.getDocumentTypeId().equals(EDocumentType.ORDER.getId()) || row.getDocumentTypeId().equals(EDocumentType.INDICATION.getId()))
			return ECHDocumentType.MEDICAL_PRESCRIPTIONS;
		if (row.getDocumentTypeId().equals(EDocumentType.EMERGENCY_CARE.getId()) || row.getDocumentTypeId().equals(EDocumentType.IMMUNIZATION.getId()))
			return ECHDocumentType.OTHER;
		return ECHDocumentType.CLINICAL_NOTES;
	}

}

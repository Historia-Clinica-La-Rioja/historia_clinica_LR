package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.documents.application.ClinicHistoryStorage;

import net.pladema.clinichistory.documents.domain.CHDocumentSummaryBo;

import net.pladema.clinichistory.documents.domain.ECHDocumentType;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import lombok.AllArgsConstructor;

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

	public ClinicHistoryStorageImpl(VClinicHistoryRepository vClinicHistoryRepository,
									EntityManager entityManager,
									SharedStaffPort sharedStaffPort,
									FeatureFlagsService featureFlagsService)
	{
		this.repository = vClinicHistoryRepository;
		this.entityManager = entityManager;
		this.sharedStaffPort = sharedStaffPort;
		this.featureFlagsService = featureFlagsService;
	}

	@Override
	public List<CHDocumentSummaryBo> getPatientClinicHistory(Integer patientId, LocalDate from, LocalDate to) {

		List<VClinicHistory> resultList = repository.getPatientClinicHistory(patientId, LocalDateTime.of(from, LocalTime.MIN), LocalDateTime.of(to, LocalTime.MAX));

		return  resultList
				.stream()
				.map(this::mapToSummaryBo)
				.collect(Collectors.toList());
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
		result.setEncounterType(mapEncounterType(row));
		result.setDocumentType(mapDocumentType(row));
		result.setProblems(mapProblems(row.getHealthConditionSummary().getProblems()));
		return result;
	}

	private String mapProblems(String problems){
		if(problems.isBlank()) return problems;
		int startIndex = problems.contains("Principal:") ? problems.indexOf("Principal:") + 11 : problems.indexOf("Otro:") + 6;
		int endIndex = problems.contains(", Otro:") ? problems.indexOf(", Otro:") : problems.length();
		return problems.substring(startIndex, endIndex);
	}

	private ECHEncounterType mapEncounterType (VClinicHistory row){
		if(row.getSourceTypeId().equals(ESourceType.HOSPITALIZATION.getId())
			|| (row.getSourceTypeId().equals(ESourceType.ORDER.getId()) && (row.getRequestSourceTypeId().equals(ESourceType.HOSPITALIZATION.getId()))))
			return ECHEncounterType.HOSPITALIZATION;
		if(row.getSourceTypeId().equals(ESourceType.EMERGENCY_CARE.getId()))
			return ECHEncounterType.EMERGENCY_CARE;
		return ECHEncounterType.OUTPATIENT;
	}

	private ECHDocumentType mapDocumentType(VClinicHistory row){
		if (row.getDocumentTypeId().equals(EDocumentType.EPICRISIS.getId()))
			return ECHDocumentType.EPICRISIS;
		if (row.getDocumentTypeId().equals(EDocumentType.RECIPE.getId()) || row.getDocumentTypeId().equals(EDocumentType.ORDER.getId()) || row.getDocumentTypeId().equals(EDocumentType.INDICATION.getId()))
			return ECHDocumentType.MEDICAL_PRESCRIPTIONS;
		if (row.getDocumentTypeId().equals(EDocumentType.EMERGENCY_CARE.getId()) || row.getDocumentTypeId().equals(EDocumentType.IMMUNIZATION.getId()))
			return ECHDocumentType.OTHER;
		return ECHDocumentType.CLINICAL_NOTES;
	}

}

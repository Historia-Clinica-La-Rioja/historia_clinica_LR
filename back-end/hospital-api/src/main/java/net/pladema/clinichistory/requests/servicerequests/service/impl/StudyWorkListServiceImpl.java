package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.servicerequests.repository.StudyWorkListRepository;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import net.pladema.vademecum.domain.SnomedBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class StudyWorkListServiceImpl implements StudyWorkListService {

	private final StudyWorkListRepository studyWorkListRepository;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public List<StudyOrderWorkListBo> execute(Integer institutionId, List<String> categories){

		log.debug("Input parameters -> institutionId: {}, categories: {}", institutionId, categories);

		String statusId = EDiagnosticReportStatus.REGISTERED.getId();
		List<Short> sourceTypeIds = List.of(
				ESourceType.HOSPITALIZATION.getId(),
				ESourceType.EMERGENCY_CARE.getId()
		);
		Short documentType = EDocumentType.ORDER.getId();

		List<StudyOrderWorkListBo> result = studyWorkListRepository.execute(institutionId, categories, sourceTypeIds, statusId, documentType)
				.stream()
				.map(this::createStudyOrderWorkListBo)
				.collect(Collectors.toList());

		log.debug("Output -> {}", result);

		return result;
	}

	private StudyOrderWorkListBo createStudyOrderWorkListBo(Object[] row) {
		boolean featureFlagEnabled = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);

		StudyOrderBasicPatientBo patientBo = StudyOrderBasicPatientBo.builder()
				.id((Integer) row[1])
				.firstName((featureFlagEnabled && row[6] != null) ? (String) row[6] : (String) row[2])
				.middleNames((String) row[3])
				.lastName((String) row[4])
				.otherLastNames((String) row[5])
				.identificationNumber((String) row[7])
				.identificationTypeId((Short) row[8])
				.genderId((Short) row[9])
				.genderDescription((String) row[10])
				.birthDate(row[11] != null ? ((java.sql.Date) row[11]).toLocalDate() : null)
				.build();

		return new StudyOrderWorkListBo(
				(Integer) row[0],
				patientBo,
				new SnomedBo((String) row[12], (String) row[13]),
				row[14] instanceof Short ? (Short) row[14] : null,
				row[15] instanceof Boolean ? (Boolean) row[15] : null,
				row[16] instanceof Short ? (Short) row[16] : null,
				row[17] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[17]).toLocalDateTime() : null,
				(String) row[19],
				row[18] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[18]).toLocalDateTime() : null
		);
	}
}

package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ESourceType;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderBasicPatientBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderPatientLocationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.servicerequests.repository.StudyWorkListRepository;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.vademecum.domain.SnomedBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class StudyWorkListServiceImpl implements StudyWorkListService {

	private final StudyWorkListRepository studyWorkListRepository;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public Page<StudyOrderWorkListBo> execute(Integer institutionId, List<String> categories, Pageable pageable){

		log.debug("Input parameters -> institutionId: {}, categories: {}, pageable: {}", institutionId, categories, pageable);

		String statusId = EDiagnosticReportStatus.REGISTERED.getId();
		List<Short> sourceTypeIds = List.of(
				ESourceType.HOSPITALIZATION.getId(),
				ESourceType.EMERGENCY_CARE.getId()
		);
		Short documentType = EDocumentType.ORDER.getId();
		Short emergencyCareState = EEmergencyCareState.ATENCION.getId();
		Short internmentEpisodeState = 1;

		var rawGroupedResults =
				studyWorkListRepository.execute(institutionId, categories, sourceTypeIds, statusId, documentType, emergencyCareState, internmentEpisodeState)
				.stream()
				.collect(Collectors.groupingBy(x -> (Integer) x[0]));

		var result = rawGroupedResults.values()
				.stream()
				.map(groupedValues -> {
			var studyOrderWorkListBo = createStudyOrderWorkListBo(groupedValues.get(0));
			var snomedList = groupedValues.stream().map(StudyWorkListServiceImpl::mapSnomed).collect(Collectors.toList());
			studyOrderWorkListBo.setSnomed(snomedList);
			return studyOrderWorkListBo;
		}).collect(Collectors.toList());

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), result.size());
		List<StudyOrderWorkListBo> paginatedList = result.subList(start, end);

		Page<StudyOrderWorkListBo> resultPage = new PageImpl<>(paginatedList, pageable, result.size());

		log.debug("Output -> {}", resultPage);

		return resultPage;
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

		StudyOrderPatientLocationBo locationBo = StudyOrderPatientLocationBo.builder()
				.bedNumber((String) row[20])
				.roomNumber((String) row[21])
				.sector((String) row[22])
				.build();

		return new StudyOrderWorkListBo(
				(Integer) row[0],
				patientBo,
				new ArrayList<>(),
				row[14] instanceof Short ? (Short) row[14] : null,
				row[15] instanceof Boolean ? (Boolean) row[15] : null,
				row[16] instanceof Short ? (Short) row[16] : null,
				row[17] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[17]).toLocalDateTime() : null,
				(String) row[19],
				row[18] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[18]).toLocalDateTime() : null,
				locationBo
		);
	}

	private static SnomedBo mapSnomed(Object[] row) {
		return new SnomedBo((String) row[12], (String) row[13]);
	}
}

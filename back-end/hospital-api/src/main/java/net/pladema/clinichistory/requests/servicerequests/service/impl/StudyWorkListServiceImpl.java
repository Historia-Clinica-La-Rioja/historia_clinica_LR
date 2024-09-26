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
import net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

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

		List<StudyOrderWorkListBo> result= studyWorkListRepository.execute(institutionId,categories, sourceTypeIds, statusId, documentType)
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());

		log.debug("Output -> {}", result);

		return result;
	};

	private StudyOrderWorkListBo mapToBo(StudyOrderWorkListVo studyOrderWorkListVo){
		boolean featureFlagEnabled = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);

		StudyOrderBasicPatientBo patientBo = StudyOrderBasicPatientBo.builder()
				.id(studyOrderWorkListVo.getPatientVo().getId())
				.firstName(studyOrderWorkListVo.getPatientVo().getFirstName())
				.lastName(studyOrderWorkListVo.getPatientVo().getLastName())
				.identificationNumber(studyOrderWorkListVo.getPatientVo().getIdentificationNumber())
				.middleNames(featureFlagEnabled ? studyOrderWorkListVo.getPatientVo().getMiddleNames() : null)
				.otherLastNames(featureFlagEnabled ? studyOrderWorkListVo.getPatientVo().getOtherLastNames() : null)
				.nameSelfDetermination(featureFlagEnabled ? studyOrderWorkListVo.getPatientVo().getNameSelfDetermination() : null)
				.genderId(studyOrderWorkListVo.getPatientVo().getGenderId())
				.genderDescription(studyOrderWorkListVo.getPatientVo().getGenderDescription())
				.birthDate(studyOrderWorkListVo.getPatientVo().getBirthDate())
				.build();

		return new StudyOrderWorkListBo(
				studyOrderWorkListVo.getStudyId(),
				patientBo,
				studyOrderWorkListVo.getSnomed(),
				studyOrderWorkListVo.getStudyTypeId(),
				studyOrderWorkListVo.getRequiresTransfer(),
				studyOrderWorkListVo.getSourceTypeId(),
				studyOrderWorkListVo.getDeferredDate(),
				studyOrderWorkListVo.getStatus(),
				studyOrderWorkListVo.getCreatedDate()
		);
	}


}

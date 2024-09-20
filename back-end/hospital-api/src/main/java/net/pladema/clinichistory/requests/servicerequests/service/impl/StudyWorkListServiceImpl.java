package net.pladema.clinichistory.requests.servicerequests.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.servicerequests.repository.StudyWorkListRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo;
import net.pladema.clinichistory.requests.servicerequests.service.StudyWorkListService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class StudyWorkListServiceImpl implements StudyWorkListService {

	private final StudyWorkListRepository studyWorkListRepository;

	@Override
	public List<StudyOrderWorkListBo> execute(Integer institutionId, List<String> categories){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<StudyOrderWorkListBo> result= studyWorkListRepository.execute(institutionId,categories)
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	};

	private StudyOrderWorkListBo mapToBo(StudyOrderWorkListVo studyOrderWorkListVo){
		return new StudyOrderWorkListBo(
				studyOrderWorkListVo.getStudyId(),
				studyOrderWorkListVo.getSnomed(),
				studyOrderWorkListVo.getStudyTypeId(),
				studyOrderWorkListVo.isRequiresTransfer(),
				studyOrderWorkListVo.getSourceTypeId(),
				studyOrderWorkListVo.getDeferredDate(),
				studyOrderWorkListVo.getStatus()
		);
	}


}

package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.requests.servicerequests.repository.domain.StudyOrderWorkListVo;

import java.util.List;

public interface StudyWorkListRepository {
	List<StudyOrderWorkListVo> execute(Integer institutionId, List<String> categories);
}

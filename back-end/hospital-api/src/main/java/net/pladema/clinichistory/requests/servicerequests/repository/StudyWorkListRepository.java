package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListFilterBo;

import java.util.List;

public interface StudyWorkListRepository {
	List<Object[]> execute(Integer institutionId, StudyOrderWorkListFilterBo filterBo, String statusId, Short documentType, Short emergencyCareState, Short internmentEpisodeSate);
}

package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;

import java.util.List;

public interface StudyWorkListService {
	List<StudyOrderWorkListBo> execute();
}


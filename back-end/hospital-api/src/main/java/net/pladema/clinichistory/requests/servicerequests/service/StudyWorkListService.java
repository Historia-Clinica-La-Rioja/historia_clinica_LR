package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;

import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListFilterBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyWorkListService {
	Page<StudyOrderWorkListBo> execute(Integer institutionId, StudyOrderWorkListFilterBo filterBo, Pageable pageable);
}


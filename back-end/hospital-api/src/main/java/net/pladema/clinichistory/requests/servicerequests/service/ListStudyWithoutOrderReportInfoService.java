package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ListStudyWithoutOrderReportInfoService {
    List<StudyWithoutOrderReportInfoBo> execute(Integer patientId);
}

package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;

import java.util.List;

public interface ListStudyWithoutOrderReportInfoService {
    List<StudyWithoutOrderReportInfoBo> execute(Integer patientId);
}

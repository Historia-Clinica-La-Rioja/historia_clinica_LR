package net.pladema.clinichistory.requests.servicerequests.service;


import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;

import java.util.List;

public interface ListStudyOrderReportInfoService {

    List<StudyOrderReportInfoBo> getListStudyOrder(Integer patientId);
}

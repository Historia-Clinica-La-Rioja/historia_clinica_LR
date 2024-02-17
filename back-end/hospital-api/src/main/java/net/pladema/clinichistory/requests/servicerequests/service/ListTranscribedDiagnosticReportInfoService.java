package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;

import java.util.List;

public interface ListTranscribedDiagnosticReportInfoService {
    List<TranscribedDiagnosticReportBo> execute(Integer patientId);

    TranscribedDiagnosticReportBo getByAppointmentId(Integer patientId);

    List<StudyTranscribedOrderReportInfoBo> getListTranscribedOrder(Integer patientId);
}

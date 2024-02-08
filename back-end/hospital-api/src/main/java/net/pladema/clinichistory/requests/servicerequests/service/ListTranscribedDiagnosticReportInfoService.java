package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import java.util.List;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;

public interface ListTranscribedDiagnosticReportInfoService {
    List<TranscribedServiceRequestBo> execute(Integer patientId);

    TranscribedServiceRequestBo getByAppointmentId(Integer appointmentId);

    List<StudyTranscribedOrderReportInfoBo> getListStudyTranscribedOrderReports(Integer patientId);
}

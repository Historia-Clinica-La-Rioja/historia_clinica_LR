package net.pladema.clinichistory.requests.transcribed.application.port;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import java.util.List;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;

public interface TranscribedServiceRequestStorage {

    TranscribedServiceRequestBo get(Integer transcribedServiceRequestId);

    Integer getIdByAppointmentId(Integer appointmentId);

    List<StudyTranscribedOrderReportInfoBo> getListStudyTranscribedOrderReportInfo(Integer patientId);

    List<Integer> getIds(Integer patientId);

    List<DiagnosticReportBo> getDiagnosticReports(Integer transcribedServiceRequestId);

    void deleteById(Integer transcribedServiceRequestId);

    void deleteDiagnosticReportIdRelatedTo(Integer transcribedServiceRequestId, Integer diagnosticReportId);
}

package net.pladema.clinichistory.requests.servicerequests.repository;


import java.util.List;

public interface ListTranscribedDiagnosticReportRepository {
    List<Object[]> execute(Integer patientId);
}

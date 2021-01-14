package net.pladema.clinichistory.requests.servicerequests.repository;

public interface GetDiagnosticReportInfoRepository {
    Object[] execute(Integer drId);
}

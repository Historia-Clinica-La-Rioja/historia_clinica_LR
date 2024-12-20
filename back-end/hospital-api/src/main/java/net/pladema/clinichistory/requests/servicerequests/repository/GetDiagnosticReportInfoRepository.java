package net.pladema.clinichistory.requests.servicerequests.repository;

public interface GetDiagnosticReportInfoRepository {
    Object[] execute(Integer drId);

	Object[] getDiagnosticReportByAppointmentId(Integer apId);

	Integer getDiagnosticReportIdByServiceRequestId(Integer serviceRequestId);

}

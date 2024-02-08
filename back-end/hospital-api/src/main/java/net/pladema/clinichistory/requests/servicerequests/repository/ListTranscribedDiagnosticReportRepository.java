package net.pladema.clinichistory.requests.servicerequests.repository;


import java.util.List;

public interface ListTranscribedDiagnosticReportRepository {
    List<Integer> execute(Integer patientId);
	List<Integer> getByAppointmentId(Integer appointmentId);
	List<Object[]> getListTranscribedOrder(Integer patientId);
}

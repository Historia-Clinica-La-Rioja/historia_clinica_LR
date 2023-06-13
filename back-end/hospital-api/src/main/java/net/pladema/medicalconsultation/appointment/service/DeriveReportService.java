package net.pladema.medicalconsultation.appointment.service;

public interface DeriveReportService {

    boolean execute(Integer institutionId, Integer appointmentId);
}

package net.pladema.medicalconsultation.appointment.controller.service;

import java.time.LocalDate;

public interface AppointmentExternalService {

    boolean hasCurrentAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date);

	boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId);

    Integer serveAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

    Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);
}

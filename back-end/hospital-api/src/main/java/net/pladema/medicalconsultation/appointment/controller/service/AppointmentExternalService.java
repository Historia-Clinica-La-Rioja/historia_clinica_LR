package net.pladema.medicalconsultation.appointment.controller.service;

import java.time.LocalDate;

public interface AppointmentExternalService {

    boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date);

    void serveAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

    Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);
}

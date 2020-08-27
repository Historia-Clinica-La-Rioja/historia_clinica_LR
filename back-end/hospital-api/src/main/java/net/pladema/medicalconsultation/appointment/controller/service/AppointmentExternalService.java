package net.pladema.medicalconsultation.appointment.controller.service;

public interface AppointmentExternalService {

    boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId);
}

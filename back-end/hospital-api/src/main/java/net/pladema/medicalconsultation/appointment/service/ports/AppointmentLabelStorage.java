package net.pladema.medicalconsultation.appointment.service.ports;

public interface AppointmentLabelStorage {

	boolean updateLabel(Integer diaryLabelId, Integer appointmentId);
}

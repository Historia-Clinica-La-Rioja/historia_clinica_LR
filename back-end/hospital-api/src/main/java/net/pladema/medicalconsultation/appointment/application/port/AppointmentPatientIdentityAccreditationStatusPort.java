package net.pladema.medicalconsultation.appointment.application.port;

public interface AppointmentPatientIdentityAccreditationStatusPort {

	void save(Integer appointmentId, short patientIdentityAccreditationStatusId, String patientIdentificationHash);

	void clearAppointmentPatientPreviousIdentificationHashByAppointmentId(Integer appointmentId);

	boolean patientIdentityAlreadyAccredited(Integer appointmentId);

}

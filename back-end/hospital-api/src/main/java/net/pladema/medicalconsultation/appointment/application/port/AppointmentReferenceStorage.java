package net.pladema.medicalconsultation.appointment.application.port;

import ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo;

public interface AppointmentReferenceStorage {

	ReferencePhoneBo getReferencePhoneData(Integer referenceId);

	void associateReferenceToAppointment(Integer referenceId, Integer appointmentId, Boolean isProtected);
}

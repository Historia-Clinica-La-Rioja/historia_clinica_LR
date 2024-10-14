package net.pladema.medicalconsultation.appointment.application.port;

import net.pladema.medicalconsultation.appointment.domain.GetAppointmentReferenceClosureTypeBo;

import java.util.Optional;

public interface AppointmentReferenceCounterReferencePort {

	Optional<GetAppointmentReferenceClosureTypeBo> getReferenceClosureTypeByAppointmentId(Integer appointmentId);

}

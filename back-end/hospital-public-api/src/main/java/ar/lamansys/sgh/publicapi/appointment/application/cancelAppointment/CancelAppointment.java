package ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment;

import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.exception.CancelAppointmentAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CancelAppointment {
	private final SharedAppointmentPort appointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public void run(Integer institutionId, Integer appointmentId, String reason){
		assertUserCanAccess(institutionId);
		appointmentPort.cancelAppointment(institutionId, appointmentId, reason);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!appointmentPublicApiPermissions.canCancelAppointment(institutionId))
			throw new CancelAppointmentAccessDeniedException();
	}
}

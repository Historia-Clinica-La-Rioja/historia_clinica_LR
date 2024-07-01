package ar.lamansys.sgh.publicapi.appointment.application.cancelBooking;

import ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.exception.CancelBookingAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CancelBookingByInstitution {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public void run(Integer institutionId, String uuid){
		assertUserCanAccess(institutionId);
		bookAppointmentPort.cancelBooking(uuid);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!appointmentPublicApiPermissions.canAccessCancelBookingByInstitution(institutionId))
			throw new CancelBookingAccessDeniedException();
	}

}

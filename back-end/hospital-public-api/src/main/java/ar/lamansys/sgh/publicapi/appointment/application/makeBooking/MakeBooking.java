package ar.lamansys.sgh.publicapi.appointment.application.makeBooking;

import ar.lamansys.sgh.publicapi.appointment.application.makeBooking.exception.MakeBookingAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MakeBooking {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public SavedBookingAppointmentDto run(Integer institutionId, BookingDto bookingDto) throws ProfessionalAlreadyBookedException, SaveExternalBookingException, BookingPersonMailNotExistsException {
		assertUserCanAccess(institutionId);
		return bookAppointmentPort.makeBooking(bookingDto,true);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!appointmentPublicApiPermissions.canAccessMakeBooking(institutionId))
			throw new MakeBookingAccessDeniedException();
	}
}

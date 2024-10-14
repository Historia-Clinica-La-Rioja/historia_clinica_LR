package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.exception.FetchBookingSpecialtiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingSpecialties {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingSpecialtyDto> run(){
		assertUserCanAccess();
		return bookAppointmentPort.fetchBookingSpecialties();
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingSpecialties())
			throw new FetchBookingSpecialtiesAccessDeniedException();
	}
}

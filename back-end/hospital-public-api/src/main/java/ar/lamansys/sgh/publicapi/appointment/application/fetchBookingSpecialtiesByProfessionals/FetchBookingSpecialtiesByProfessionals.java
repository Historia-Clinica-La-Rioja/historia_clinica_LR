package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingSpecialtiesByProfessionals;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingSpecialtiesByProfessionals.exception.FetchBookingSpecialtiesByProfessionalsAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingSpecialtiesByProfessionals {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingSpecialtyDto> run(){
		assertUserCanAccess();
		return bookAppointmentPort.fetchBookingSpecialtiesByProfessionals();
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingSpecialties())
			throw new FetchBookingSpecialtiesByProfessionalsAccessDeniedException();
	}
}

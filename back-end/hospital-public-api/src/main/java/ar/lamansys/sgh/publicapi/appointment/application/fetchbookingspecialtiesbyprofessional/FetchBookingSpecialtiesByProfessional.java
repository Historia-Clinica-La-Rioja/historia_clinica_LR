package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.exception.FetchBookingSpecialtiesByProfessionalAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingSpecialtiesByProfessional {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingSpecialtyDto> run(Integer healthcareProfessionalId){
		assertUserCanAccess();
		return bookAppointmentPort.fetchBookingSpecialtiesByProfessional(healthcareProfessionalId);
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingSpecialtiesByProfessional())
			throw new FetchBookingSpecialtiesByProfessionalAccessDeniedException();
	}
}

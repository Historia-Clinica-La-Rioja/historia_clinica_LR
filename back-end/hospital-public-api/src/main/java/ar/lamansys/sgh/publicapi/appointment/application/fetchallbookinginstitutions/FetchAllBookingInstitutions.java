package ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions;

import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.exception.FetchAllBookingInstitutionsAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchAllBookingInstitutions {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingInstitutionDto> run(){
		assertUserCanAccess();
		return bookAppointmentPort.fetchAllBookingInstitutions();
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutions())
			throw new FetchAllBookingInstitutionsAccessDeniedException();
	}


}

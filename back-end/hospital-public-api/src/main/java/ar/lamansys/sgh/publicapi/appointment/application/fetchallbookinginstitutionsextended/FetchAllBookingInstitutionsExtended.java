package ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended;

import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.exception.FetchAllBookingInstitutionsExtendedAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionExtendedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchAllBookingInstitutionsExtended {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingInstitutionExtendedDto> run(){
		assertUserCanAccess();
		return bookAppointmentPort.fetchAllBookingInstitutionsExtended();
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutionsExtended())
			throw new FetchAllBookingInstitutionsExtendedAccessDeniedException();
	}
}

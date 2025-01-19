package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.exception.FetchBookingProfessionalsAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingProfessionalsByInstitution {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingProfessionalDto> run(Integer institutionId, Integer medicalCoverageId, boolean all){
		assertUserCanAccess(institutionId);
		return bookAppointmentPort.fetchBookingProfessionals(institutionId,medicalCoverageId, all);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingProfessionals(institutionId))
			throw new FetchBookingProfessionalsAccessDeniedException();
	}

}

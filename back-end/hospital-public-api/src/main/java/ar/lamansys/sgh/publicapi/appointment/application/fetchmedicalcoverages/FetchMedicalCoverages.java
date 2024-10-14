package ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages;

import ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.exception.FetchMedicalCoveragesAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchMedicalCoverages {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<BookingHealthInsuranceDto> run(){
		assertUserCanAccess();
		return bookAppointmentPort.fetchMedicalCoverages();
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchMedicalCoverages())
			throw new FetchMedicalCoveragesAccessDeniedException();
	}

}

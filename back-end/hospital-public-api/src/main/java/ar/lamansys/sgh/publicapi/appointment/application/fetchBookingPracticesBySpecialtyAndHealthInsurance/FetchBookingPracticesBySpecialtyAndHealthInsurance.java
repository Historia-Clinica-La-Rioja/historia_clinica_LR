package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.exception.FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingPracticesBySpecialtyAndHealthInsurance {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<PracticeDto> run(Integer clinicalSpecialtyId,
								 Integer medicalCoverageId, boolean all){
		assertUserCanAccess();
		return bookAppointmentPort.fetchBookingPracticesBySpecialtyAndHealthInsurance(clinicalSpecialtyId, medicalCoverageId, all);
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingPracticesBySpecialtyAndHealthInsurance())
			throw new FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException();
	}
}

package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage;

import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage.exception.AvailabilityByProfessionalAndCoverageAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentAvailabilityPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FetchAvailabilityByProfessionalAndCoverage {

	private final SharedBookingPort sharedBookingPort;

	private final AppointmentAvailabilityPublicApiPermissions appointmentAvailabilityPublicApiPermissions;

	public ProfessionalAvailabilityDto run(Integer institutionId, Integer healthcareProfessionalId,
										   Integer clinicalSpecialtyId, Integer practiceId, Integer coverageId, String maxDate) {
		assertUserCanAccess(institutionId);
		return sharedBookingPort.fetchAvailabilityByPracticeAndProfessional(institutionId, healthcareProfessionalId, clinicalSpecialtyId,
				practiceId, coverageId, maxDate);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if(!appointmentAvailabilityPublicApiPermissions.canCheckAvailability(institutionId)){
			throw new AvailabilityByProfessionalAndCoverageAccessDeniedException();
		}
	}
}

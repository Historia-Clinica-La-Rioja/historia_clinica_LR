package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyspeciality;

import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyspeciality.exception.AppointmentAvailabilityAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentAvailabilityPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchAvailabilityBySpecialty {

	private final SharedBookingPort sharedBookingPort;

	private final AppointmentAvailabilityPublicApiPermissions appointmentAvailabilityPublicApiPermissions;

	public List<ProfessionalAvailabilityDto> run(Integer institutionId, Integer clinicalSpecialtyId, Integer practiceId, Integer medicalCoverageId) {
		assertUserCanAccess(institutionId);
		return sharedBookingPort.fetchAvailabilityByPractice(institutionId, clinicalSpecialtyId, practiceId, medicalCoverageId);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if(!appointmentAvailabilityPublicApiPermissions.canCheckAvailability(institutionId)){
			throw new AppointmentAvailabilityAccessDeniedException();
		}
	}


}

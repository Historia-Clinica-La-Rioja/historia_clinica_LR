package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.exception.FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchBookingPracticesByProfessionalAndHealthInsurance {
	private final SharedBookingPort bookAppointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<PracticeDto> run(Integer healthcareProfessionalId, Integer clinicalSpecialtyId,
								 Integer medicalCoverageId, boolean all){
		assertUserCanAccess();
		return bookAppointmentPort.fetchBookingPracticesByProfessionalAndHealthInsurance(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
	}

	private void assertUserCanAccess() {
		if (!appointmentPublicApiPermissions.canAccessFetchBookingPracticesByProfessionalAndHealthInsurance())
			throw new FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException();
	}
}

package ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution;

import ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.exception.AppointmentAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class FetchAppointmentsByInstitution {

	private final SharedAppointmentPort appointmentPort;
	private final AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	public List<PublicAppointmentListDto> run(Integer institutionId, String identificationNumber,
											  LocalDate startDate,
											  LocalDate endDate){
		assertUserCanAccess(institutionId);
		return appointmentPort.fetchAppointments(institutionId, identificationNumber, null, startDate, endDate);
	}

	private void assertUserCanAccess(Integer institutionId) {
		if (!appointmentPublicApiPermissions.canAccessAppointment(institutionId))
			throw new AppointmentAccessDeniedException();
	}
}

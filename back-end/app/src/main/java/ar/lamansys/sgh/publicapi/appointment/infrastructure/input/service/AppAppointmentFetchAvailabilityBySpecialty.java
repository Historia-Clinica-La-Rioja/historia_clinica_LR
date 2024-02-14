package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppAppointmentFetchAvailabilityBySpecialty implements AppointmentAvailabilityPublicApiPermissions {

	private final UserSessionStorage userSessionStorage;

	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canCheckAvailability(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssignment -> roleAssignment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssignment)
		);
	}
}

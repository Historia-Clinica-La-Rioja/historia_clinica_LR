package ar.lamansys.sgh.publicapi.patient.input.service;

import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppPatientInformationPublicApiPermission implements PatientInformationPublicApiPermission {
	private final UserSessionStorage userSessionStorage;

	@Override
	public boolean canAccessPersonFromIdPatient() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_PACIENTES, -1)
		);
	}
}

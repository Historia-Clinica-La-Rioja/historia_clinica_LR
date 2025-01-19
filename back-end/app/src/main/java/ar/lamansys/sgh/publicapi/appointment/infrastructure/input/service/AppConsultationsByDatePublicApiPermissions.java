package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service;

import ar.lamansys.sgh.publicapi.reports.infrastructure.input.service.ConsultationsByDatePublicApiPermissions;
import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.InstitutionRepository;

import net.pladema.permissions.repository.enums.ERole;

import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AppConsultationsByDatePublicApiPermissions implements ConsultationsByDatePublicApiPermissions {

	private final InstitutionRepository institutionRepository;

	private final UserSessionStorage userSessionStorage;

	@Override
	public boolean canFetchConsultations() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_REPORTES, -1)
		);
	}

	@Override
	public Optional<Integer> getInstitutionId(String refsetCode) {
		return institutionRepository.findIdBySisaCode(refsetCode);
	}
}

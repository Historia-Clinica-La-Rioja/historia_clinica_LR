package ar.lamansys.sgh.publicapi.reports.infrastructure.input.service;

import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AppFetchDailyHoursByDatePublicApiPermissions implements DailyHoursByDatePublicApiPermissions{

	private final InstitutionRepository institutionRepository;

	private final UserSessionStorage userSessionStorage;

	@Override
	public Optional<Integer> getInstitutionId(String refsetCode) {
		return institutionRepository.findIdBySisaCode(refsetCode);
	}

	@Override
	public boolean canFetchDailyHoursByDate() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_REPORTES, -1)
		);
	}
}

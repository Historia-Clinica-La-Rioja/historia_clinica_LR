package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeBedValidator implements BackofficePermissionValidator<Bed, Integer> {

	private final BedRepository repository;

	private final RoomRepository roomRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	public BackofficeBedValidator(BedRepository repository,
								  RoomRepository roomRepository,
                                  BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
                                  PermissionEvaluator permissionEvaluator) {
		this.repository = repository;
		this.roomRepository = roomRepository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
	}


	@Override
	public void assertGetList(Bed entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(entity.getId());
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public List<Integer> filterByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return ids.stream().filter(id -> {
			try {
				hasPermissionByInstitution(id);
				return true;
			} catch (Exception e) {
				return false;
			}
		}).collect(Collectors.toList());
	}

	@Override
	public void assertGetOne(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertCreate(Bed entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = roomRepository.getInstitutionId(entity.getRoomId());
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertUpdate(Integer id, Bed entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	private void hasPermissionByInstitution(Integer institutionId) {
		if (institutionId == null)
			return;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
	}

}

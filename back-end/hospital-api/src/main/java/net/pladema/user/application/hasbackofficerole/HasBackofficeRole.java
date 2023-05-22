package net.pladema.user.application.hasbackofficerole;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.user.application.port.UserRoleStorage;

@RequiredArgsConstructor
@Service
@Slf4j
public class HasBackofficeRole {
	private final UserRoleStorage userRoleStorage;

	public Boolean execute(Integer userId) {
		log.debug("Input parameters -> {}", userId);
		Boolean result = userRoleStorage.getRolesByUser(userId).stream()
				.filter(userRoleBo -> (
						userRoleBo.getRoleId() == ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.getId() ||
								userRoleBo.getRoleId() == ERole.ADMINISTRADOR.getId())
				).collect(Collectors.toList()).stream().findAny().isPresent();
		log.debug("Output ->{}", result);
		return result;
	}
}

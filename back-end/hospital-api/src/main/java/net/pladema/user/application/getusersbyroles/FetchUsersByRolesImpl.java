package net.pladema.user.application.getusersbyroles;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.domain.PersonDataBo;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class FetchUsersByRolesImpl implements FetchUsersByRoles {

	private final UserRoleStorage userRoleStorage;

	@Override
	public List<PersonDataBo> execute(Integer institutionId, List<Short> rolesId) {
		log.debug("Input parameter -> institutionId {}, rolesId {}", institutionId, rolesId);
		return userRoleStorage.getUsersByRoles(institutionId, rolesId);
	}
}

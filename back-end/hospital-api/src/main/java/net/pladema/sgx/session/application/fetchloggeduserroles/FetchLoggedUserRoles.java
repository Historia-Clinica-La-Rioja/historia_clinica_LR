package net.pladema.sgx.session.application.fetchloggeduserroles;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@Service
public class FetchLoggedUserRoles {
	private final UserSessionStorage userSessionStorage;

	public FetchLoggedUserRoles(
			UserSessionStorage userSessionStorage
	) {
		this.userSessionStorage = userSessionStorage;
	}

	public Stream<RoleAssignment> execute() {
		return userSessionStorage.getRolesAssigned();
	}
}

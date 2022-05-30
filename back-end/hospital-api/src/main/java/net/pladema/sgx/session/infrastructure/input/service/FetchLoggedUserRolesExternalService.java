package net.pladema.sgx.session.infrastructure.input.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.session.application.fetchloggeduserroles.FetchLoggedUserRoles;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchLoggedUserRolesExternalService {

	private final FetchLoggedUserRoles fetchLoggedUserRoles;

	public Stream<RoleAssignment> execute(){
		log.debug("Executing FetchLoggedUserRolesExternalService");
		return fetchLoggedUserRoles.execute();
	}
}

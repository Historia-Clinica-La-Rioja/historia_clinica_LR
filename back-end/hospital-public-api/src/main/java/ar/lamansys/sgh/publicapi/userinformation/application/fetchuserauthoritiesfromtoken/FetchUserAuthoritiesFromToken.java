package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken;

import java.util.List;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken.exception.UserAuthoritiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.domain.authorities.PublicAuthorityBo;

import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.application.port.out.PublicUserStorage;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FetchUserAuthoritiesFromToken {

	private final PublicUserStorage publicUserStorage;
	private final UserInformationPublicApiPermission userInformationPublicApiPermission;

	public List<PublicAuthorityBo> execute(String token) {
		assertUserCanAccess();
		return publicUserStorage.fetchRolesFromToken(token);
	}

	private void assertUserCanAccess() {
		if(!userInformationPublicApiPermission.canAccessUserAuthoritiesFromToken())
			throw new UserAuthoritiesAccessDeniedException();
	}
}

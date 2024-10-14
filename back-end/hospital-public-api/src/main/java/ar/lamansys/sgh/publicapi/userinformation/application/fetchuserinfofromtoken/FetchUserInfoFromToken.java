package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken.exception.UserInfoFromTokenAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.PublicUserStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.PublicUserInfoBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class FetchUserInfoFromToken {

	private final PublicUserStorage publicUserStorage;
	private final UserInformationPublicApiPermission userInformationPublicApiPermission;

	public Optional<PublicUserInfoBo> execute(String token) {
		assertUserCanAccess();
		return publicUserStorage.fetchUserInfoFromToken(token);
	}

	private void assertUserCanAccess() {
		if(!userInformationPublicApiPermission.canAccessUserInfoFromToken())
			throw new UserInfoFromTokenAccessDeniedException();
	}
}

package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserInformationAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserNotExistsException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.FetchUserPersonFromTokenStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

@Service
public class FetchUserPersonFromToken {

	private final FetchUserPersonFromTokenStorage fetchUserPersonFromTokenStorage;

	private final UserInformationPublicApiPermission userInformationPublicApiPermission;


	public FetchUserPersonFromToken(FetchUserPersonFromTokenStorage fetchUserPersonFromTokenStorage,
									UserInformationPublicApiPermission userInformationPublicApiPermission) {
		this.fetchUserPersonFromTokenStorage = fetchUserPersonFromTokenStorage;
		this.userInformationPublicApiPermission = userInformationPublicApiPermission;
	}

	public FetchUserPersonFromTokenBo run(String userToken) {
		assertUserCanAccess();
		var info = this.fetchUserPersonFromTokenStorage.getUserPersonFromToken(userToken);
		if(info.isEmpty()) {
			throw new UserNotExistsException();
		}
		else {
			return info.get();
		}
	}

	private void assertUserCanAccess() {
		if (!userInformationPublicApiPermission.canAccess()) {
			throw new UserInformationAccessDeniedException();
		}
	}
}

package net.pladema.sgx.session.application.fetchloggeduserinfo;

import org.springframework.stereotype.Service;

import net.pladema.authorization.application.port.UserPersonStorage;
import net.pladema.authorization.application.port.exceptions.InvalidUserException;
import net.pladema.authorization.domain.UserPersonaBo;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@Service
public class FetchLoggedUserInfo {
	private final UserSessionStorage userSessionStorage;
	private final UserPersonStorage userPersonaStorage;

	public FetchLoggedUserInfo(
			UserSessionStorage userSessionStorage,
			UserPersonStorage userPersonaStorage
	) {
		this.userSessionStorage = userSessionStorage;
		this.userPersonaStorage = userPersonaStorage;
	}

	public UserPersonaBo execute() throws InvalidUserException {
		return userPersonaStorage.fetchUserPerson(userSessionStorage.getUserId());
	}
}

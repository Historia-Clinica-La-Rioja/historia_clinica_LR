package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class UserInfoFromTokenAccessDeniedException extends PublicApiAccessDeniedException {

	public UserInfoFromTokenAccessDeniedException() {
		super("UserInformation", "FetchUserInfoFromToken");
	}
}

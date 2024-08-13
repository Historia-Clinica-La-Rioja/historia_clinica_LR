package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class UserAuthoritiesAccessDeniedException extends PublicApiAccessDeniedException {

	public UserAuthoritiesAccessDeniedException() {
		super("User", "FetchUserAuthoritiesFromToken");
	}

}

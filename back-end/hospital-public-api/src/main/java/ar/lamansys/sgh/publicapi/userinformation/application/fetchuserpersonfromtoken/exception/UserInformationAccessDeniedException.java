package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class UserInformationAccessDeniedException extends PublicApiAccessDeniedException{

	public UserInformationAccessDeniedException() {
			super("UserInformation", "FetchUserPersonFromToken");
		}

}

package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class UserInformationAccessDeniedException extends PublicApiAccessDeniedException{

	public UserInformationAccessDeniedException() {
			super("UserInformation", "FetchUserPersonFromToken");
		}

}

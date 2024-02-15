package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public class UserNotExistsException extends NotFoundException {
	public UserNotExistsException(){
		super("user-not-exists", "El usuario no existe");
	}
}

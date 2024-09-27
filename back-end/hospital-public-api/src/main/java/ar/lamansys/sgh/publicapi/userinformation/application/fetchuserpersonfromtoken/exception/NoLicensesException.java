package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public class NoLicensesException extends NotFoundException {
	public NoLicensesException(){
		super("no-licenses", "El usuario no tiene licencias asociadas");
	}
}

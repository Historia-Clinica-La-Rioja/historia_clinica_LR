package ar.lamansys.sgh.publicapi.activities.staff.application.exception;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public class InstitutionNotExistsException extends NotFoundException {
	public InstitutionNotExistsException() {
		super("institution-not-exists", "La institución no existe");
	}

}

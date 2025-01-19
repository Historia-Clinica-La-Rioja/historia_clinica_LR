package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public class InstitutionNotFoundException extends NotFoundException {

	public InstitutionNotFoundException() {
		super("institution-not-exists", "La institución no existe");
	}
}

package ar.lamansys.sgh.publicapi.activities.staff.application.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchHealthcareProfessionalsDeniedException extends PublicApiAccessDeniedException {

	public FetchHealthcareProfessionalsDeniedException() {
		super("Staff", "FetchHealthcareProfessionalsByInstitution");
	}

}
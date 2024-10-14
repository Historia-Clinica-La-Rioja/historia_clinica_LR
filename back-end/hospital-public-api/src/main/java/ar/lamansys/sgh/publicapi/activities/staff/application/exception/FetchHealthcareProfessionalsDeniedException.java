package ar.lamansys.sgh.publicapi.activities.staff.application.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchHealthcareProfessionalsDeniedException extends PublicApiAccessDeniedException {

	public FetchHealthcareProfessionalsDeniedException() {
		super("Staff", "FetchHealthcareProfessionalsByInstitution");
	}

}
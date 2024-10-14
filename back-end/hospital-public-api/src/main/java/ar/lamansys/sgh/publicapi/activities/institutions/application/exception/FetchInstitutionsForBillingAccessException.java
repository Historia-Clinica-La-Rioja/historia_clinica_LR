package ar.lamansys.sgh.publicapi.activities.institutions.application.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchInstitutionsForBillingAccessException extends PublicApiAccessDeniedException {
	public FetchInstitutionsForBillingAccessException() {
		super("Institutions", "FetchInstitutionsForBillingDeniedAccess");
	}
}

package ar.lamansys.sgh.publicapi.activities.medicalcoverages.application.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchAllMedicalCoveragesDeniedAccessException extends PublicApiAccessDeniedException {
	public FetchAllMedicalCoveragesDeniedAccessException() {
		super("MedicalCoverages", "FetchAllMedicalCoveragesDeniedAccess");
	}
}

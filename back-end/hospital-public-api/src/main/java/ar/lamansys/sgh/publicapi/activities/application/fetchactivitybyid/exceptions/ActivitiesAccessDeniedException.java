package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class ActivitiesAccessDeniedException extends PublicApiAccessDeniedException {
	public ActivitiesAccessDeniedException() {
		super("Activities", "FetchActivity");
	}
}

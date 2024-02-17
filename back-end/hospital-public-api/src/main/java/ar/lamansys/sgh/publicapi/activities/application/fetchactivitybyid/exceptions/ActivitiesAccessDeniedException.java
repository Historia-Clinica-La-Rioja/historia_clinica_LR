package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class ActivitiesAccessDeniedException extends PublicApiAccessDeniedException {
	public ActivitiesAccessDeniedException() {
		super("Activities", "FetchActivity");
	}
}

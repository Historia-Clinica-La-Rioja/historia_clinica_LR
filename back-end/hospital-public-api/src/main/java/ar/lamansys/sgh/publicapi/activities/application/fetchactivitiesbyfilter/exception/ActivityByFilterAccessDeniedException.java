package ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class ActivityByFilterAccessDeniedException extends PublicApiAccessDeniedException {

	public ActivityByFilterAccessDeniedException() {
		super("Activities","FetchActivitiesByFilter");
	}
}

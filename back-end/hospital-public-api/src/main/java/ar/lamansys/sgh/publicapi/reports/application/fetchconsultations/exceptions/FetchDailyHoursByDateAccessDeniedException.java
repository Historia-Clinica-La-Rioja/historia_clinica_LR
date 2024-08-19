package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchDailyHoursByDateAccessDeniedException extends PublicApiAccessDeniedException {
	public FetchDailyHoursByDateAccessDeniedException() {
		super("DailyHours", "FetchDailyHoursByDate");
	}
}

package ar.lamansys.sgh.publicapi.activities.holidays.application.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchHolidaysForBillingAccessException extends PublicApiAccessDeniedException {
	public FetchHolidaysForBillingAccessException() {
		super("Holidays", "FetchHolidaysForBillingDeniedAccess");
	}
}

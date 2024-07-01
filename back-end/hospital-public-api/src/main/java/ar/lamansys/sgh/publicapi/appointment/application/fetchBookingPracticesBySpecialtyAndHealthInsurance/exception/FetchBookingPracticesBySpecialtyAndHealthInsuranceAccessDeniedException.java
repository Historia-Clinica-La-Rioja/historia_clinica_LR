package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException(){
		super("Appointment","FetchBookingPracticesBySpecialityAndHealthInsurance");
	}
}

package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException(){
		super("Appointment","FetchBookingPracticesBySpecialityAndHealthInsurance");
	}
}

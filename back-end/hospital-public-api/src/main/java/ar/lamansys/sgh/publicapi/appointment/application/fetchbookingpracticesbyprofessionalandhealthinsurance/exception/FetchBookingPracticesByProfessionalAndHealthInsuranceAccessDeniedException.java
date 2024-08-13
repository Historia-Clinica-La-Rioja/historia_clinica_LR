package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException() {
		super("Appointment", "FetchBookingPracticesByProfessionalAndHealthInsurance");
	}
}

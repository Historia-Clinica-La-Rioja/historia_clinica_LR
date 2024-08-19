package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchConsultationsAccessDeniedException extends PublicApiAccessDeniedException {
	public FetchConsultationsAccessDeniedException() {
		super("Consultations", "FetchConsultations");
	}
}

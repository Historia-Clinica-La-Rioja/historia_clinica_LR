package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchConsultationsAccessDeniedException extends PublicApiAccessDeniedException {
	public FetchConsultationsAccessDeniedException() {
		super("Consultations", "FetchConsultations");
	}
}

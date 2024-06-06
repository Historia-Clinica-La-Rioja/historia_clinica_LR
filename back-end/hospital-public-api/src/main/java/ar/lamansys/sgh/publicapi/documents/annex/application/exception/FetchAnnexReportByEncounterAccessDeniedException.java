package ar.lamansys.sgh.publicapi.documents.annex.application.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchAnnexReportByEncounterAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchAnnexReportByEncounterAccessDeniedException() {
		super("Documents", "Fetch annex report by encounter");
	}
}

package ar.lamansys.sgh.publicapi.documents.annex.application.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchAnnexReportByEncounterAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchAnnexReportByEncounterAccessDeniedException() {
		super("Documents", "Fetch annex report by encounter");
	}
}

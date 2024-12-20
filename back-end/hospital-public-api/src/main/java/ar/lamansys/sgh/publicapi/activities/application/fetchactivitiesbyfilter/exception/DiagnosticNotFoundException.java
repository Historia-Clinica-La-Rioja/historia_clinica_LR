package ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception;


import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class DiagnosticNotFoundException extends PublicApiAccessDeniedException {

	public DiagnosticNotFoundException() {
		super("Activities", "FetchActivitiesByFilter");
	}
}
package ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class DeleteExternalEncounterAccessDeniedException extends PublicApiAccessDeniedException {

	public DeleteExternalEncounterAccessDeniedException() {
		super("Patient","DeleteExternalEncounter");
	}
}

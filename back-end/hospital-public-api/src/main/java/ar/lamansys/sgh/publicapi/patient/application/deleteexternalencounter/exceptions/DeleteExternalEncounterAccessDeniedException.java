package ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class DeleteExternalEncounterAccessDeniedException extends PublicApiAccessDeniedException {

	public DeleteExternalEncounterAccessDeniedException() {
		super("Patient","DeleteExternalEncounter");
	}
}

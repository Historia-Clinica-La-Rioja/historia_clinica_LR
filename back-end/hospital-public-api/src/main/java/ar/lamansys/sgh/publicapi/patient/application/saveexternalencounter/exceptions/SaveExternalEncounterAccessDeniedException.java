package ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class SaveExternalEncounterAccessDeniedException extends PublicApiAccessDeniedException {

	public SaveExternalEncounterAccessDeniedException() {
		super("Patient","SaveExternalEncounter");
	}
}

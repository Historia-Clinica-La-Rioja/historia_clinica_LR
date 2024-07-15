package ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class SaveExternalClinicHistoryAccessDeniedException extends PublicApiAccessDeniedException {

	public SaveExternalClinicHistoryAccessDeniedException() {
		super("Patient","SaveExternalClinicHistory");
	}
}

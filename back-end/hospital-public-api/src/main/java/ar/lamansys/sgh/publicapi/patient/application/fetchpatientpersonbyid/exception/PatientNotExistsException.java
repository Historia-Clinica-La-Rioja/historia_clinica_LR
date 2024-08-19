package ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public class PatientNotExistsException extends NotFoundException {
	public PatientNotExistsException() {
		super("patient-not-exists", "El paciente no existe");
	}
}

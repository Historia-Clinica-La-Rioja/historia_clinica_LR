package ar.lamansys.sgh.publicapi.patient.application.port.out.exceptions;

import lombok.Getter;

@Getter
public class ExternalClinicalHistoryStorageException extends RuntimeException {

	public final ExternalClinicalHistoryStorageExceptionEnum code;

	public ExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
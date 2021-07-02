package ar.lamansys.immunization.application.immunizePatient.exceptions;

import lombok.Getter;

@Getter
public class ImmunizePatientException extends RuntimeException {
	public final ImmunizePatientExceptionEnum code;

	public ImmunizePatientException(ImmunizePatientExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

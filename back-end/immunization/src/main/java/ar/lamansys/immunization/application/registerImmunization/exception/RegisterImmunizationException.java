package ar.lamansys.immunization.application.registerImmunization.exception;

import lombok.Getter;

@Getter
public class RegisterImmunizationException extends RuntimeException {
	public final RegisterImmunizationExceptionEnum code;

	public RegisterImmunizationException(RegisterImmunizationExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

package ar.lamansys.immunization.domain.immunization;

import lombok.Getter;

@Getter
public class ImmunizationValidatorException extends RuntimeException {
	public final ImmunizationValidatorExceptionEnum code;

	public ImmunizationValidatorException(ImmunizationValidatorExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

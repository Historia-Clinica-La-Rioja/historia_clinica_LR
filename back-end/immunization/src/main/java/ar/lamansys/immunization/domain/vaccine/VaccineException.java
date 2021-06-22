package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;

@Getter
public class VaccineException extends RuntimeException {
	public final VaccineExceptionEnum code;

	public VaccineException(VaccineExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

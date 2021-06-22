package ar.lamansys.immunization.domain.vaccine.doses;

import lombok.Getter;

@Getter
public class VaccineDosisException extends RuntimeException {
	public final VaccineDosisExceptionEnum code;

	public VaccineDosisException(VaccineDosisExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

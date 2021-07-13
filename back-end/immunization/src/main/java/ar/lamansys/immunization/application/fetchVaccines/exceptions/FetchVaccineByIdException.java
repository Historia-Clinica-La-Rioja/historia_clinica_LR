package ar.lamansys.immunization.application.fetchVaccines.exceptions;

import lombok.Getter;

@Getter
public class FetchVaccineByIdException extends RuntimeException {
	public final FetchVaccineByIdExceptionEnum code;

	public FetchVaccineByIdException(FetchVaccineByIdExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

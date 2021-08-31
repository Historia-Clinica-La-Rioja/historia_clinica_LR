package ar.lamansys.immunization.application.fetchVaccineSchemeInfo.exceptions;

import lombok.Getter;

@Getter
public class FetchVaccineSchemeException extends RuntimeException {
	public final FetchVaccineSchemeExceptionEnum code;

	public FetchVaccineSchemeException(FetchVaccineSchemeExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

package ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.exceptions;

import lombok.Getter;

@Getter
public class FetchVaccineConditionApplicationException extends RuntimeException {
	public final FetchVaccineConditionApplicationExceptionEnum code;

	public FetchVaccineConditionApplicationException(FetchVaccineConditionApplicationExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

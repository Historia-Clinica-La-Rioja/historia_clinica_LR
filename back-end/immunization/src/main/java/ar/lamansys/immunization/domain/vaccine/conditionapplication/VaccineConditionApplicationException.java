package ar.lamansys.immunization.domain.vaccine.conditionapplication;

import lombok.Getter;

@Getter
public class VaccineConditionApplicationException extends RuntimeException {
	public final VaccineConditionApplicationExceptionEnum code;

	public VaccineConditionApplicationException(VaccineConditionApplicationExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

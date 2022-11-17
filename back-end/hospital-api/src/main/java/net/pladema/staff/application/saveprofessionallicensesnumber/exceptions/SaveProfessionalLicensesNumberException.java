package net.pladema.staff.application.saveprofessionallicensesnumber.exceptions;

import lombok.Getter;

@Getter
public class SaveProfessionalLicensesNumberException extends RuntimeException {

	private final SaveProfessionalLicensesNumberEnumException code;

	public SaveProfessionalLicensesNumberException(SaveProfessionalLicensesNumberEnumException code, String message) {
		super(message);
		this.code = code;
	}
}


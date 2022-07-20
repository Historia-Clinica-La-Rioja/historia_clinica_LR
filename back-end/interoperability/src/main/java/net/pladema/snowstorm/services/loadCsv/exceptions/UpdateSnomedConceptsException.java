package net.pladema.snowstorm.services.loadCsv.exceptions;

import lombok.Getter;

@Getter
public class UpdateSnomedConceptsException extends RuntimeException {

	private final UpdateSnomedConceptsExceptionEnum code;

	public UpdateSnomedConceptsException(UpdateSnomedConceptsExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

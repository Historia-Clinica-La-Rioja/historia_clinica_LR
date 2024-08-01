package net.pladema.snowstorm.services.loadCsv.exceptions;

import lombok.Getter;

@Getter
public class UpdateSnomedConceptsException extends RuntimeException {

	private final EUpdateSnomedConceptsException code;

	public UpdateSnomedConceptsException(EUpdateSnomedConceptsException code, String message) {
		super(message);
		this.code = code;
	}
}

package net.pladema.clinichistory.hospitalization.service.impl.exceptions;

import lombok.Getter;

@Getter
public class InternmentDocumentException extends RuntimeException {

	private final InternmentDocumentEnumException code;

	public InternmentDocumentException(InternmentDocumentEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

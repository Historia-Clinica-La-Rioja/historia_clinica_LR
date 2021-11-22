package net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateOutpatientDocumentException extends RuntimeException {
	private final CreateOutpatientDocumentExceptionEnum code;

	private List<String> messages = new ArrayList<>();

	public CreateOutpatientDocumentException(CreateOutpatientDocumentExceptionEnum code) {
		super();
		this.code = code;
	}

	public void addError(String message) {
		messages.add(message);
	}

	public boolean hasErrors(){
		return !messages.isEmpty();
	}
}

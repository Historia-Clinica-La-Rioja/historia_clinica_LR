package net.pladema.person.controller.service.exceptions;

import lombok.Getter;

@Getter
public class CreatePersonFileException extends RuntimeException {

	public final CreatePersonFileExceptionEnum code;

	public CreatePersonFileException(CreatePersonFileExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

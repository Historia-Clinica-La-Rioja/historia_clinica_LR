package net.pladema.person.controller.service.exceptions;

import lombok.Getter;

@Getter
public class CreatePersonException extends RuntimeException {

	private final CreatePersonEnumException code;

	public CreatePersonException(CreatePersonEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

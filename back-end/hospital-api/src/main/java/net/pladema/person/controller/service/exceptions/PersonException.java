package net.pladema.person.controller.service.exceptions;

import lombok.Getter;

@Getter
public class PersonException extends RuntimeException {

	public final PersonEnumException code;

	public PersonException(PersonEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

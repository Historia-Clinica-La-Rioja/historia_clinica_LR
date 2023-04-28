package ar.lamansys.pac.infrastructure.input.rest.exceptions;

import lombok.Getter;

@Getter
public enum StudyAccessExceptionEnum {

	MALFORMED("Asegúrese estar enviando un Bearer token facilitado por el sistema"),
	UNAUTHORIZED("El token enviado se encuentra malformado o expiró");
	private final String message;

	StudyAccessExceptionEnum(String message) {
		this.message = message;
	}
}

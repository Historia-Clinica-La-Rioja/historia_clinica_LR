package ar.lamansys.pac.application.exception;

import lombok.Getter;

@Getter
public enum PacExceptionEnum {

	MALFORMED("Asegúrese estar enviando un Bearer token facilitado por el sistema"),
	UNAUTHORIZED("El token enviado se encuentra malformado o expiró");
	private final String message;

	PacExceptionEnum(String message) {
		this.message = message;
	}
}

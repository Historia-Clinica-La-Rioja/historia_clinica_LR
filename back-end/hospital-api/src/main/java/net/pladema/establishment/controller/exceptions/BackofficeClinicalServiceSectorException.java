package net.pladema.establishment.controller.exceptions;

import lombok.Getter;

@Getter
public class BackofficeClinicalServiceSectorException extends RuntimeException{

	private final BackofficeClinicalServiceSectorEnumException code;

	public BackofficeClinicalServiceSectorException(BackofficeClinicalServiceSectorEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

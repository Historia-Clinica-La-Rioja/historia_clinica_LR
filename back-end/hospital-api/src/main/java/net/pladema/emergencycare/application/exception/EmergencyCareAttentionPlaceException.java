package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public class EmergencyCareAttentionPlaceException extends RuntimeException{

	private final EmergencyCareAttentionPlaceExceptionEnum code;

	public EmergencyCareAttentionPlaceException(EmergencyCareAttentionPlaceExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

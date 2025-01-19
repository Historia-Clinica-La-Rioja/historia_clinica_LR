package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public class EmergencyCareEpisodeStateException extends RuntimeException {

	private final EmergencyCareEpisodeStateExceptionEnum code;

	public EmergencyCareEpisodeStateException(EmergencyCareEpisodeStateExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}

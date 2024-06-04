package net.pladema.emergencycare.controller.exceptions;

import lombok.Getter;

@Getter
public class SaveEmergencyCareEpisodeException extends RuntimeException {

	private final SaveEmergencyCareEpisodeExceptionEnum code;

	public SaveEmergencyCareEpisodeException(SaveEmergencyCareEpisodeExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
}

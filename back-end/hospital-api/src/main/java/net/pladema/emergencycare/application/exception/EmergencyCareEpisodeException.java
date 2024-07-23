package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public class EmergencyCareEpisodeException extends RuntimeException {

	private static final long serialVersionUID = -3550978559705822167L;

	private final EmergencyCareEpisodeExcepcionEnum code;

    public EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum code, String message) {
        super(message);
        this.code = code;
    }
}

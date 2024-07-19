package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public class EmergencyCareEpisodeException extends RuntimeException {

    private final EmergencyCareEpisodeExcepcionEnum code;

    public EmergencyCareEpisodeException(EmergencyCareEpisodeExcepcionEnum code, String message) {
        super(message);
        this.code = code;
    }
}

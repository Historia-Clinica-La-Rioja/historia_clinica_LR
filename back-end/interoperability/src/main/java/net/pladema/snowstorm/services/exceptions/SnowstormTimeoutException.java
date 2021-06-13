package net.pladema.snowstorm.services.exceptions;

import lombok.Getter;

@Getter
public class SnowstormTimeoutException extends RuntimeException {

    private SnowstormEnumException code;

    public SnowstormTimeoutException(SnowstormEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
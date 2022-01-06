package net.pladema.snowstorm.services.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class SnowstormApiException extends Exception {

    private SnowstormEnumException code;

    private HttpStatus statusCode;

    public SnowstormApiException(SnowstormEnumException snowstormEnumException, HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.code = snowstormEnumException;
    }
}
package net.pladema.federar.services.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;


@Getter
public class FederarApiException extends Exception {

    private FederarEnumException code;

    private HttpStatus statusCode;

    public FederarApiException(FederarEnumException federarEnumException, HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.code = federarEnumException;
    }
}
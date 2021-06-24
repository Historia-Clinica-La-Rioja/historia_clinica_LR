package ar.lamansys.sgx.auth.jwt.application.login.exceptions;

import lombok.Getter;

@Getter
public class BadLoginException extends Exception {

    private final BadLoginEnumException code;

    public BadLoginException(BadLoginEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

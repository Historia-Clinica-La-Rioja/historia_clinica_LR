package ar.lamansys.sgx.auth.user.application.registeruser.exceptions;

import lombok.Getter;

@Getter
public class RegisterUserException extends RuntimeException {

    private final RegisterUserEnumException code;

    public RegisterUserException(RegisterUserEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
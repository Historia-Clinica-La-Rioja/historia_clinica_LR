package net.pladema.user.controller.service.createDefaultUser.exceptions;

import lombok.Getter;

@Getter
public class CreateDefaultUserException extends RuntimeException {

    private final CreateDefaultUserExceptionEnum code;

    public CreateDefaultUserException(CreateDefaultUserExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}

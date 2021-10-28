package net.pladema.user.controller.service.UpdateEnable.exceptions;

import lombok.Getter;

@Getter
public class UpdateEnableException extends RuntimeException {

    private final UpdateEnableExceptionEnum code;

    public UpdateEnableException(UpdateEnableExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}

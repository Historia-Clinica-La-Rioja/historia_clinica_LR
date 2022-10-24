package net.pladema.user.application.port.exceptions;

import lombok.Getter;

@Getter
public class UserPersonStorageException extends RuntimeException {

    private final UserPersonStorageEnumException code;

    public UserPersonStorageException(UserPersonStorageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

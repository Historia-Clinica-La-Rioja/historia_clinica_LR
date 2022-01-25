package net.pladema.user.application.port.exceptions;

import lombok.Getter;

@Getter
public class UserRoleStorageException extends RuntimeException {
    private final UserRoleStorageEnumException code;

    public UserRoleStorageException(UserRoleStorageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

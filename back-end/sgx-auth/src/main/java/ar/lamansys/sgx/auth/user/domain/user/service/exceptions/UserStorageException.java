package ar.lamansys.sgx.auth.user.domain.user.service.exceptions;

import lombok.Getter;

@Getter
public class UserStorageException extends RuntimeException {

    private final UserStorageEnumException code;

    public UserStorageException(UserStorageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
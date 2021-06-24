package ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions;

import lombok.Getter;

@Getter
public class PasswordResetTokenStorageException extends RuntimeException {

    private final PasswordResetTokenStorageEnumException code;

    public PasswordResetTokenStorageException(PasswordResetTokenStorageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
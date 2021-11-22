package ar.lamansys.sgx.auth.user.domain.user.model;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final UserEnumException code;

    public UserException(UserEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
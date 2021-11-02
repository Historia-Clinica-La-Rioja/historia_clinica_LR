package ar.lamansys.sgx.auth.user.domain.userpassword;

import lombok.Getter;

@Getter
public class UserPasswordException extends RuntimeException {

    private final UserPasswordEnumException code;

    public UserPasswordException(UserPasswordEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
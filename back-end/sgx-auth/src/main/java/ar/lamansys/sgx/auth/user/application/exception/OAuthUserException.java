package ar.lamansys.sgx.auth.user.application.exception;

import lombok.Getter;

@Getter
public class OAuthUserException extends RuntimeException {

    private final OAuthUserEnumException code;

    public OAuthUserException(OAuthUserEnumException code, String message) {
        super(message);
        this.code = code;
    }

}

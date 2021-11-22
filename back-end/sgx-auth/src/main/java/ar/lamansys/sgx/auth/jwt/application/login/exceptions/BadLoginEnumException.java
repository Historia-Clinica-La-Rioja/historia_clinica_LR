package ar.lamansys.sgx.auth.jwt.application.login.exceptions;

import lombok.Getter;

@Getter
public enum BadLoginEnumException {
    BAD_CREDENTIALS,
    DISABLED_USER;

    BadLoginEnumException() {
    }

}

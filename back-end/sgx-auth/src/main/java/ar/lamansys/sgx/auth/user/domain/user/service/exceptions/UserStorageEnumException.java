package ar.lamansys.sgx.auth.user.domain.user.service.exceptions;

import lombok.Getter;

@Getter
public enum UserStorageEnumException {
    NOT_FOUND_USER,
    NOT_FOUND_USER_PASSWORD,
    DUPLICATE_USER
    ;

}

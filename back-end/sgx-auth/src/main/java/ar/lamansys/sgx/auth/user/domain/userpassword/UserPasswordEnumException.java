package ar.lamansys.sgx.auth.user.domain.userpassword;

import lombok.Getter;

@Getter
public enum UserPasswordEnumException {
    NULL_CREATION_TIME,
    NULL_PASSWORD,
    NULL_SALT,
    NULL_HASH_ALGORITHM,
    ;

}

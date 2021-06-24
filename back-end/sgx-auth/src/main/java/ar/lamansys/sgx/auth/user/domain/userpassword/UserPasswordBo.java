package ar.lamansys.sgx.auth.user.domain.userpassword;

import lombok.Getter;

import java.util.Objects;

@Getter
public class UserPasswordBo {

    private final String password;

    private final String salt;

    private final String hashAlgorithm;

    public UserPasswordBo(String password, String salt, String hashAlgorithm) {
        validations(password, salt, hashAlgorithm);
        this.password = password;
        this.salt = salt;
        this.hashAlgorithm = hashAlgorithm;
    }

    private void validations(String password, String salt, String hashAlgorithm) {
        Objects.requireNonNull(password, () -> {
            throw new UserPasswordException(UserPasswordEnumException.NULL_PASSWORD, "La contraseña es obligatoria");
        });

        Objects.requireNonNull(salt, () -> {
            throw new UserPasswordException(UserPasswordEnumException.NULL_SALT, "El salt es obligatorio");
        });

        Objects.requireNonNull(hashAlgorithm, () -> {
            throw new UserPasswordException(UserPasswordEnumException.NULL_HASH_ALGORITHM, "El algoritmo de hashing es obligatorio");
        });
    }
}

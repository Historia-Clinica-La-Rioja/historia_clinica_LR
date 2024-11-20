package ar.lamansys.sgx.auth.user.domain.passwordreset;

import ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions.PasswordResetTokenStorageException;

public interface PasswordResetTokenStorage {

    PasswordResetTokenBo get(String token) throws PasswordResetTokenStorageException;

    void disableTokens(Integer userId);

    PasswordResetTokenBo createToken(Integer userId);
}

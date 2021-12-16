package ar.lamansys.sgx.auth.user.domain.passwordreset;

public interface PasswordResetTokenStorage {

    PasswordResetTokenBo get(String token);

    void disableTokens(Integer userId);

    PasswordResetTokenBo createToken(Integer userId);
}

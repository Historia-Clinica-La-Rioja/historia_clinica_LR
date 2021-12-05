package ar.lamansys.sgx.auth.user.application.createtokenpasswordreset;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateTokenPasswordResetImpl implements CreateTokenPasswordReset {

    private final Logger logger;

    private final PasswordResetTokenStorage passwordResetTokenStorage;

    public CreateTokenPasswordResetImpl(PasswordResetTokenStorage passwordResetTokenStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.passwordResetTokenStorage = passwordResetTokenStorage;
    }

    @Override
    public String execute(Integer userId) {
        logger.debug("Input parameter -> {}", userId);
        return passwordResetTokenStorage.createToken(userId).getToken();
    }
}

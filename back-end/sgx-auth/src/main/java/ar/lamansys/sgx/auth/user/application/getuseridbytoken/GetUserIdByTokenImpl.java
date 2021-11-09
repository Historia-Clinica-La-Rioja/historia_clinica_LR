package ar.lamansys.sgx.auth.user.application.getuseridbytoken;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetUserIdByTokenImpl implements GetUserIdByToken {

    private final Logger logger;

    private final PasswordResetTokenStorage passwordResetTokenStorage;

    public GetUserIdByTokenImpl(PasswordResetTokenStorage passwordResetTokenStorage) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.passwordResetTokenStorage = passwordResetTokenStorage;
    }


    @Override
    public Integer execute(String token) {
        Integer result = passwordResetTokenStorage.get(token).getUserId();
        logger.debug("Output -> {}", result);
        return result;
    }
}

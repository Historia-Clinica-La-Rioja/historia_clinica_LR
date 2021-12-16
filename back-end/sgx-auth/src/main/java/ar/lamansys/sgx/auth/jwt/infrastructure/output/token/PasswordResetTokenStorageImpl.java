package ar.lamansys.sgx.auth.jwt.infrastructure.output.token;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions.PasswordResetTokenStorageEnumException;
import ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions.PasswordResetTokenStorageException;
import ar.lamansys.sgx.auth.user.infrastructure.output.userpassword.PasswordResetToken;
import ar.lamansys.sgx.auth.user.infrastructure.output.userpassword.PasswordResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenStorageImpl implements PasswordResetTokenStorage {

    private final Logger logger;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${password.reset.token.expiration}")
    private Duration passwordExpiration;

    public PasswordResetTokenStorageImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.logger =  LoggerFactory.getLogger(getClass());
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordResetTokenBo get(String token) {
        logger.debug("Get password reset token from database {} ", token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new PasswordResetTokenStorageException(PasswordResetTokenStorageEnumException.TOKEN_NOT_FOUND, String.format("Token %s inexistente ", token)));
        return mapPasswordResetTokenBo(passwordResetToken);
    }

    @Override
    public PasswordResetTokenBo createToken(Integer userId) {
        logger.debug("Create password reset token for userId {} ",userId);
        PasswordResetToken entity = new PasswordResetToken();
        entity.setUserId(userId);
        entity.setExpiryDate(LocalDateTime.now().plusSeconds(passwordExpiration.getSeconds()));
        entity.setToken(UUID.randomUUID().toString());
        entity.setEnable(true);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.save(entity);
        return mapPasswordResetTokenBo(passwordResetToken);
    }

    private PasswordResetTokenBo mapPasswordResetTokenBo(PasswordResetToken passwordResetToken) {
        return new PasswordResetTokenBo(passwordResetToken.getId(),
                        passwordResetToken.getToken(),
                        passwordResetToken.getUserId(),
                        passwordResetToken.getEnable(),
                        passwordResetToken.getExpiryDate());
    }

    @Override
    public void disableTokens(Integer userId) {
        logger.debug("Disable token to userId {} ", userId);
        passwordResetTokenRepository.disableTokens(userId);
    }
}

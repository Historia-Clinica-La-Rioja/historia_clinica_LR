package ar.lamansys.sgx.auth.user.infrastructure.output.userpassword;

import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordBCryptImpl implements PasswordEncryptor {

    private final Logger logger;

    private final BCryptPasswordEncoder encoder;

    public PasswordBCryptImpl() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(String rawPassword, String salt, String hashAlgorithm) {
        logger.debug("Encode password");
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String password, String password1) {
        logger.debug("Match passwords");
        return encoder.matches(password, password1);
    }
}

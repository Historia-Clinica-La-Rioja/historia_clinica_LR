package ar.lamansys.sgx.auth.user.application.updatepassword;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdatePasswordImpl implements UpdatePassword {

    private final Logger logger;

    private final UserStorage userStorage;

    private final PasswordEncryptor passwordEncryptor;
    public UpdatePasswordImpl(UserStorage userStorage,
                              PasswordEncryptor passwordEncryptor) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.passwordEncryptor = passwordEncryptor;
        this.userStorage = userStorage;
    }

    @Override
    public void execute(String username, String password) {
        logger.debug("Update password username -> {}", username);
        UserBo user = userStorage.getUser(username);
        final var salt = "salt";
        final var hashAlgorithm = "hashAlgorithm";
        user.setUserPasswordBo(passwordEncryptor.encode(password, salt, hashAlgorithm),
                salt,
                hashAlgorithm);
        userStorage.update(user);
    }
}

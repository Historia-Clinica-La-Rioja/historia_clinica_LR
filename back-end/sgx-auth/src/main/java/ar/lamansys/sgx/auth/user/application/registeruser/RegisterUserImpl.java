package ar.lamansys.sgx.auth.user.application.registeruser;

import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserEnumException;
import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class RegisterUserImpl implements RegisterUser {

    private final Logger logger;

    private final UserStorage userStorage;

    private final Pattern usernamePattern;

    private final PasswordEncryptor passwordEncryptor;

    private final String defaultPassword;

    public RegisterUserImpl(UserStorage userStorage,
                            @Value("${authentication.username.pattern:^[a-zA-Z]+[a-zA-Z0-9@.]*$}") String pattern,
                            PasswordEncryptor passwordEncryptor,
                            @Value("${authentication.username.pattern:PRUEBA}") String defaultPassword) {
        this.passwordEncryptor = passwordEncryptor;
        this.defaultPassword = defaultPassword;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.userStorage = userStorage;
        this.usernamePattern = Pattern.compile(pattern);
    }

    @Override
    public void execute(String username, String email, String password) {
        logger.debug("Register new user -> {}", username);
        validations(username);
        var salt = "salt";
        var hashAlgorithm = "hashAlgorithm";
        userStorage.save(
                new UserBo(username,
                        passwordEncryptor.encode(password != null ? password : defaultPassword, salt, hashAlgorithm),
                        salt,
                        hashAlgorithm));
    }

    private void validations(String username) {
        Objects.requireNonNull(username, () -> {
            throw new RegisterUserException(RegisterUserEnumException.NULL_USERNAME, "El username es obligatorio");
        });
        if (!usernamePattern.matcher(username).matches()){
            throw new RegisterUserException(RegisterUserEnumException.INVALID_USERNAME_PATTERN,
                    String.format("El username %s no cumple con el patrón %s obligatorio", username, usernamePattern.pattern()));
        }
    }
}

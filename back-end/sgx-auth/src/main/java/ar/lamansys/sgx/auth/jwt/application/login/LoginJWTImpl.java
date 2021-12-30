package ar.lamansys.sgx.auth.jwt.application.login;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginEnumException;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginJWTImpl implements Login {

    private final Logger logger;

    private final UserInfoStorage userInfoStorage;

    private final PasswordEncryptor passwordEncryptor;

    private final GenerateToken generateToken;

    public LoginJWTImpl(UserInfoStorage userInfoStorage,
                        PasswordEncryptor passwordEncryptor,
                        GenerateToken generateToken) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.passwordEncryptor = passwordEncryptor;
        this.generateToken = generateToken;
        this.userInfoStorage = userInfoStorage;
    }

    @Override
    public JWTokenBo execute(LoginBo login) throws BadLoginException {
        UserInfoBo user = userInfoStorage.getUser(login.username);
        if (user == null)
            throw new BadLoginException(BadLoginEnumException.BAD_CREDENTIALS, "Usuario inválido");
        if (!user.isEnable())
            throw new BadLoginException(BadLoginEnumException.DISABLED_USER, "Usuario inválido");
        if (!passwordEncryptor.matches(login.password, user.getPassword()))
            throw new BadLoginException(BadLoginEnumException.BAD_CREDENTIALS, "Usuario/contraseña inválida");
        logger.debug("User {} authenticated", login.username);
        JWTokenBo result = generateToken.generateTokens(user.getId(), user.getUsername());
        userInfoStorage.updateLoginDate(user.getUsername());
        logger.debug("Token generated ");
        return result;
    }

}

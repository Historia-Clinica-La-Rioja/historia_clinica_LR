package ar.lamansys.sgx.auth.jwt.infrastructure.input.service;

import ar.lamansys.sgx.auth.jwt.application.login.Login;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.LoginDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtExternalServiceImpl implements JwtExternalService {

    private final Logger logger;

    private final Login login;

	private final String secret;
    public JwtExternalServiceImpl(Login login,
								  @Value("${token.secret}") String secret) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.login = login;
		this.secret = secret;
    }


    @Override
    public JWTokenDto login(LoginDto loginDto) throws BadLoginException {
        logger.debug("Login attempt for user {}", loginDto.username);
        JWTokenBo resultToken = login.execute(new LoginBo(loginDto.username, loginDto.password));
        logger.debug("Token generated for user {}", loginDto.username);
        return new JWTokenDto(resultToken.token, resultToken.refreshToken);
    }

	@Override
	public Optional<Integer> fetchUserIdFromNormalToken(String token) {
		return TokenUtils.parseToken(token, secret, ETokenType.NORMAL)
				.map(tokenData -> tokenData.userId);
	}
}

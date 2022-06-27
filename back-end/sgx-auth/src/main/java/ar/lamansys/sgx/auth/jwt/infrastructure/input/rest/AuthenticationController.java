package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.jwt.application.login.Login;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.RefreshToken;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.LoginDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.RefreshTokenDto;
import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "Authorization")
public class AuthenticationController {

	private final Login login;

	private final RefreshToken refreshToken;

	private final ICaptchaService captchaService;

	@PostMapping
	public JWTokenDto login(
			@Valid @RequestBody LoginDto loginDto,
			@RequestHeader("Origin") String frontUrl,
			@RequestHeader(value = "recaptcha", required = false) String recaptcha
	) throws BadLoginException {
		if (captchaService.isRecaptchaEnable()) {
			captchaService.validRecaptcha(frontUrl, recaptcha);
		}
		log.debug("Login attempt for user {}", loginDto.username);
		JWTokenBo resultToken = login.execute(new LoginBo(loginDto.username, loginDto.password));
		log.debug("Token generated for user {}", loginDto.username);
		return new JWTokenDto(resultToken.token, resultToken.refreshToken);
	}

	@PostMapping(value = "/refresh")
	public JWTokenDto refreshToken(
			@Valid @RequestBody RefreshTokenDto refreshTokenDto
	) throws BadRefreshTokenException {
		log.debug("Refreshing token");
		JWTokenBo resultToken = refreshToken.execute(refreshTokenDto.refreshToken);
		log.debug("Token refreshed");
		return new JWTokenDto(resultToken.token, resultToken.refreshToken);
	}


}
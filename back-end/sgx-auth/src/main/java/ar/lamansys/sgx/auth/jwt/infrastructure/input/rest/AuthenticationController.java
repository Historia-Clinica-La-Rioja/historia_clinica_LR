package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest;

import javax.validation.Valid;

import ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.LoginTwoFactorAuthentication;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.TwoFactorAuthenticationLoginDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.jwt.application.cookie.CookieService;
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
	private final LoginTwoFactorAuthentication loginTwoFactorAuthentication;
	private final ICaptchaService captchaService;

	private final CookieService cookieService;


	@PostMapping
	public ResponseEntity<JWTokenDto>  login(
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
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookieService.tokenCookieHeader(resultToken.token))
				.header(HttpHeaders.SET_COOKIE, cookieService.refreshTokenCookieHeader(resultToken.refreshToken))
				.body(new JWTokenDto(resultToken.token, resultToken.refreshToken));
	}

	@PostMapping(value = "/refresh")
	public ResponseEntity<JWTokenDto> refreshToken(
			@CookieValue(name = "refreshToken", defaultValue =  "") String refreshTokenCookieValue,
			@RequestBody(required=false) RefreshTokenDto refreshTokenDto
	) throws BadRefreshTokenException {
		log.debug("Refreshing token");

		var refreshTokenValue = refreshTokenDto != null && !ObjectUtils.isEmpty(refreshTokenDto.refreshToken) ?
				refreshTokenDto.refreshToken : refreshTokenCookieValue;

		JWTokenBo resultToken = refreshToken.execute(refreshTokenValue);
		log.debug("Token refreshed");
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookieService.tokenCookieHeader(resultToken.token))
				.header(HttpHeaders.SET_COOKIE, cookieService.refreshTokenCookieHeader(resultToken.refreshToken))
				.body(new JWTokenDto(resultToken.token, resultToken.refreshToken));
	}

	@PostMapping("/login-2fa")
	@PreAuthorize("hasAnyAuthority('PARTIALLY_AUTHENTICATED')")
	public ResponseEntity<JWTokenDto> completeLoginWith2FA(@RequestBody TwoFactorAuthenticationLoginDto loginDto) throws BadLoginException {
		JWTokenBo resultToken = loginTwoFactorAuthentication.execute(loginDto.getCode());
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookieService.tokenCookieHeader(resultToken.token))
				.header(HttpHeaders.SET_COOKIE, cookieService.refreshTokenCookieHeader(resultToken.refreshToken))
				.body(new JWTokenDto(resultToken.token, resultToken.refreshToken));
	}

	@DeleteMapping(value = "/refresh")
	public ResponseEntity<Void> logout() {
		return cookieService.deleteTokensResponse(HttpStatus.OK).build();
	}

}
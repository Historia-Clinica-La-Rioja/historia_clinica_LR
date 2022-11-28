package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.exceptions;

import java.util.Locale;

import ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.exceptions.BadOTPException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.auth.jwt.application.cookie.CookieService;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.auth.jwt")
public class JWTExceptionHandler {

	private final CookieService cookieService;

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ BadLoginException.class })
	protected ApiErrorMessageDto handleBadLoginException(BadLoginException ex, Locale locale) {
		log.debug("BadLoginException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}


	@ExceptionHandler({ BadRefreshTokenException.class })
	protected ResponseEntity<ApiErrorMessageDto> handleBadRefreshTokenException(BadRefreshTokenException ex, Locale locale) {
		log.debug("BadRefreshTokenException exception -> {}", ex.getMessage());
		return cookieService.deleteTokensResponse(HttpStatus.PRECONDITION_FAILED)
				.body(new ApiErrorMessageDto(null, ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadCredentialsException.class })
	public ApiErrorMessageDto invalidCredentials(BadCredentialsException ex) {
		log.warn(ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getMessage(), "Nombre de usuario o clave inválidos");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadOTPException.class })
	public ApiErrorMessageDto invalidCredentials(BadOTPException ex) {
		log.warn(ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().name(), "Código de verificación inválido");
	}

}


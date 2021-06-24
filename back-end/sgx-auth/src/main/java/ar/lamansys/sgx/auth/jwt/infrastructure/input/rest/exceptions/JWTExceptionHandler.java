package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.auth.jwt")
public class JWTExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(JWTExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ BadLoginException.class })
	protected ApiErrorMessageDto handleBadLoginException(BadLoginException ex, Locale locale) {
		LOG.debug("BadLoginException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ BadRefreshTokenException.class })
	protected ApiErrorMessageDto handleBadRefreshTokenException(BadRefreshTokenException ex, Locale locale) {
		LOG.debug("BadRefreshTokenException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}



	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadCredentialsException.class })
	public ApiErrorMessageDto invalidCredentials(BadCredentialsException ex) {
		LOG.warn(ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getMessage(), "Nombre de usuario o clave inválidos");
	}

}


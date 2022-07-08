package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.exceptions.TwoFactorAuthenticationException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.auth.twoFactorAuthentication")
@Slf4j
public class TwoFactorAuthenticationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ TwoFactorAuthenticationException.class })
	protected ApiErrorMessageDto handleTwoFactorAuthenticationException(TwoFactorAuthenticationException ex) {
		log.debug("TwoFactorAuthenticationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}

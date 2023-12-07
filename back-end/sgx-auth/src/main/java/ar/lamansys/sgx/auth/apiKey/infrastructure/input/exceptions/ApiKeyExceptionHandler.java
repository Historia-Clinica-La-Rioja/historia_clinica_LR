package ar.lamansys.sgx.auth.apiKey.infrastructure.input.exceptions;

import java.util.Locale;

import ar.lamansys.sgx.auth.apiKey.domain.exceptions.KeyNameCharacterLimitExceededException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.auth.apiKey.domain.exceptions.DuplicateKeyNameException;
import ar.lamansys.sgx.auth.apiKey.domain.exceptions.InvalidKeyNameException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.auth.apiKey")
public class ApiKeyExceptionHandler {
	private final MessageSource messageSource;

	private ApiErrorMessageDto buildErrorMessage(String erroCode, Locale locale) {
		return new ApiErrorMessageDto(
				erroCode,
				messageSource.getMessage(
						String.format("app.apikey.errors.%s", erroCode),
						null,
						String.format("Código '%s'", erroCode),
						locale
				)
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ InvalidKeyNameException.class })
	protected ApiErrorMessageDto handleInvalidKeyNameException(InvalidKeyNameException ex, Locale locale) {
		log.debug("InvalidKeyNameException exception -> {}", ex.getMessage());
		return buildErrorMessage("invalid-name", locale);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DuplicateKeyNameException.class })
	protected ApiErrorMessageDto handleDuplicateKeyNameException(DuplicateKeyNameException ex, Locale locale) {
		log.debug("DuplicateKeyNameException exception -> {}", ex.getMessage());
		return buildErrorMessage("duplicated-name", locale);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ KeyNameCharacterLimitExceededException.class })
	protected ApiErrorMessageDto handleKeyNameCharacterLimitExceededException(KeyNameCharacterLimitExceededException ex, Locale locale) {
		log.debug("KeyNameCharacterLimitExceededException exception -> {}", ex.getMessage());
		return buildErrorMessage("name-characte-limit-exceeded", locale);
	}
}


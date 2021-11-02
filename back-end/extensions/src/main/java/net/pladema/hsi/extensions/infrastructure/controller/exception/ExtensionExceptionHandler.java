package net.pladema.hsi.extensions.infrastructure.controller.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.hsi.extensions.domain.exception.ExtensionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.hsi.extensions")
public class ExtensionExceptionHandler {

	private final Logger logger;

	public ExtensionExceptionHandler() {
		logger = LoggerFactory.getLogger(ExtensionExceptionHandler.class);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ExtensionException.class })
	protected ApiErrorMessageDto handleExtensionException(ExtensionException ex, Locale locale) {
		logger.debug("ExtensionException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


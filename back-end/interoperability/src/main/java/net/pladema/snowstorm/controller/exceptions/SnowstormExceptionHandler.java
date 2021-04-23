package net.pladema.snowstorm.controller.exceptions;

import ar.lamansys.sgx.controller.dto.ApiErrorMessageDto;
import net.pladema.snowstorm.services.exceptions.SnowstormTimeoutException;
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
@RestControllerAdvice(basePackages = "net.pladema.snowstorm")
public class SnowstormExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SnowstormExceptionHandler.class);

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ SnowstormTimeoutException.class })
	protected ApiErrorMessageDto handleSnowstormTimeoutException(SnowstormTimeoutException ex, Locale locale) {
		LOG.error("SnowstormTimeoutException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


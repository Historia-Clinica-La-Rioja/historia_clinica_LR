package ar.lamansys.base.infrastructure.input.rest.exceptions.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import ar.lamansys.base.infrastructure.input.rest.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.base")
public class RestExceptionHandler {

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({ ResourceAccessException.class })
	protected ApiErrorMessageDto handleUnavailableException(ResourceAccessException ex) {
		log.debug("RuntimeException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.SERVICE_UNAVAILABLE.toString(), "Timed out. The resource is not available.");
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ RuntimeException.class })
	protected ApiErrorMessageDto handleGenericException(RuntimeException ex) {
		log.debug("RuntimeException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getCause().getLocalizedMessage());
	}
}

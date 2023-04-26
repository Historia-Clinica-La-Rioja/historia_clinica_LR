package ar.lamansys.base.infrastructure.input.rest.exceptions.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import ar.lamansys.base.infrastructure.input.rest.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order()
@RestControllerAdvice(basePackages = "ar.lamansys.base")
public class RestExceptionHandler {

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ ResourceAccessException.class })
	protected ApiErrorMessageDto handleUnavailableException(ResourceAccessException ex) {
		log.debug("RuntimeException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.REQUEST_TIMEOUT.toString(), "El recurso no se encuentra disponible");
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ RuntimeException.class })
	protected ApiErrorMessageDto handleGenericException(RuntimeException ex) {
		log.debug("RuntimeException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(), null);
	}
}

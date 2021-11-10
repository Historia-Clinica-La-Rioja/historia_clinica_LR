package net.pladema.snowstorm.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.snowstorm")
public class SnowstormExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SnowstormExceptionHandler.class);

	@ExceptionHandler({ RestTemplateApiException.class })
	protected ResponseEntity<ApiErrorMessageDto> handleRestTemplateApiException(RestTemplateApiException ex) {
		LOG.error("RestTemplateApiException exception -> {}", ex.getMessage());
		var message = new ApiErrorMessageDto(ex.getStatusCode().toString(), ex.getMessage());
		return new ResponseEntity<>(message, ex.getStatusCode());
	}
}


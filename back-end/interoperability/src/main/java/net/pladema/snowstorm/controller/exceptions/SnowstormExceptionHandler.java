package net.pladema.snowstorm.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.loadCsv.exceptions.UpdateSnomedConceptsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.snowstorm")
public class SnowstormExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SnowstormExceptionHandler.class);

	@ExceptionHandler({ SnowstormApiException.class })
	protected ResponseEntity<ApiErrorMessageDto> handleSnowstormApiException(SnowstormApiException ex) {
		LOG.error("SnowstormException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage()), ex.getStatusCode());
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler({ UpdateSnomedConceptsException.class })
	protected ApiErrorMessageDto handleUpdateSnomedConceptsException(UpdateSnomedConceptsException ex) {
		LOG.error("UpdateSnomedConceptsException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}
}


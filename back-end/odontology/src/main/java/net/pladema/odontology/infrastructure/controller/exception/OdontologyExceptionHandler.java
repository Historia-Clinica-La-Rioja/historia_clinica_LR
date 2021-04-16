package net.pladema.odontology.infrastructure.controller.exception;

import net.pladema.odontology.application.plugin.exception.ToothServiceException;
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
@RestControllerAdvice(basePackages = "net.pladema.odontology")
public class OdontologyExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OdontologyExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ ToothServiceException.class })
	protected ApiOdontologyErrorMessageDto handleToothServiceException(ToothServiceException ex, Locale locale) {
		LOG.debug("ToothServiceException exception -> {}", ex.getMessage());
		return new ApiOdontologyErrorMessageDto(null, ex.getMessage());
	}


}


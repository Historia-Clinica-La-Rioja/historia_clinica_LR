package ar.lamansys.odontology.infrastructure.controller.exception;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.odontogram.exception.ToothNotFoundException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
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
@RestControllerAdvice(basePackages = "ar.lamansys.odontology")
public class OdontologyExceptionHandler {

	private final Logger logger;

	public OdontologyExceptionHandler() {
		logger = LoggerFactory.getLogger(OdontologyExceptionHandler.class);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ ToothNotFoundException.class })
	protected ApiErrorMessageDto handleToothServiceException(ToothNotFoundException ex, Locale locale) {
		logger.debug("ToothServiceException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ CreateConsultationException.class })
	protected ApiErrorDto handleCreateConsultationException(CreateConsultationException ex, Locale locale) {
		logger.debug("CreateConsultationException exception -> {}", ex.getMessages());
		return new ApiErrorDto(ex.getCode(), ex.getMessages());
	}
}


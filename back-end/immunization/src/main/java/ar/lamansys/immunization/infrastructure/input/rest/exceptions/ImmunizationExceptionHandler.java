package ar.lamansys.immunization.infrastructure.input.rest.exceptions;

import ar.lamansys.immunization.application.FetchVaccines.exceptions.FetchVaccinesByPatientException;
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
@RestControllerAdvice(basePackages = "ar.lamansys.inmmunization")
public class ImmunizationExceptionHandler {

	private final Logger logger;

	public ImmunizationExceptionHandler() {
		logger = LoggerFactory.getLogger(ImmunizationExceptionHandler.class);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ FetchVaccinesByPatientException.class })
	protected ApiErrorMessageDto handleFetchVaccinesByPatientException(FetchVaccinesByPatientException ex, Locale locale) {
		logger.debug("FetchVaccinesByPatientException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


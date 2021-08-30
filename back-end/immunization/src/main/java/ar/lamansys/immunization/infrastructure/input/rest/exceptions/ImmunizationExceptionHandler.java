package ar.lamansys.immunization.infrastructure.input.rest.exceptions;

import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.exceptions.FetchVaccineConditionApplicationException;
import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.exceptions.FetchVaccineSchemeException;
import ar.lamansys.immunization.application.fetchVaccines.exceptions.FetchVaccineByIdException;
import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidatorException;
import ar.lamansys.immunization.domain.user.RolePermissionException;
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
@RestControllerAdvice(basePackages = "ar.lamansys.immunization")
public class ImmunizationExceptionHandler {

	private final Logger logger;

	public ImmunizationExceptionHandler() {
		logger = LoggerFactory.getLogger(ImmunizationExceptionHandler.class);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ FetchVaccineByIdException.class })
	protected ApiErrorMessageDto handleFetchVaccinesByPatientException(FetchVaccineByIdException ex, Locale locale) {
		logger.debug("FetchVaccinesByPatientException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ImmunizePatientException.class })
	protected ApiErrorMessageDto handleImmunizeException(ImmunizePatientException ex, Locale locale) {
		logger.debug("ImmunizeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ImmunizationValidatorException.class })
	protected ApiErrorMessageDto handleImmunizationValidatorException(ImmunizationValidatorException ex, Locale locale) {
		logger.debug("ImmunizationValidatorException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ FetchVaccineConditionApplicationException.class })
	protected ApiErrorMessageDto handleFetchVaccineConditionApplicationException(FetchVaccineConditionApplicationException ex, Locale locale) {
		logger.debug("FetchVaccineConditionApplicationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ FetchVaccineSchemeException.class })
	protected ApiErrorMessageDto handleFetchVaccineSchemeException(FetchVaccineSchemeException ex, Locale locale) {
		logger.debug("FetchVaccineSchemeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ RolePermissionException.class })
	protected ApiErrorMessageDto handleRolePermissionException(RolePermissionException ex, Locale locale) {
		logger.debug("RolePermissionException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}



}


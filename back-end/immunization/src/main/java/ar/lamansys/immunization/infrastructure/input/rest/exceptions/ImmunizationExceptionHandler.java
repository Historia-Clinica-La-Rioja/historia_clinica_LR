package ar.lamansys.immunization.infrastructure.input.rest.exceptions;

import ar.lamansys.immunization.application.FetchVaccines.exceptions.FetchVaccinesByPatientException;
import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.application.registerImmunization.exception.RegisterImmunizationException;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidatorException;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationException;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDosisException;
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
	@ExceptionHandler({ FetchVaccinesByPatientException.class })
	protected ApiErrorMessageDto handleFetchVaccinesByPatientException(FetchVaccinesByPatientException ex, Locale locale) {
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
	@ExceptionHandler({ VaccineConditionApplicationException.class })
	protected ApiErrorMessageDto handleVaccineConditionApplicationException(VaccineConditionApplicationException ex, Locale locale) {
		logger.debug("VaccineConditionApplicationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ VaccineDosisException.class })
	protected ApiErrorMessageDto handleVaccineDosisException(VaccineDosisException ex, Locale locale) {
		logger.debug("VaccineDosisException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RegisterImmunizationException.class })
	protected ApiErrorMessageDto handleRegisterImmunizationException(RegisterImmunizationException ex, Locale locale) {
		logger.debug("RegisterImmunizationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


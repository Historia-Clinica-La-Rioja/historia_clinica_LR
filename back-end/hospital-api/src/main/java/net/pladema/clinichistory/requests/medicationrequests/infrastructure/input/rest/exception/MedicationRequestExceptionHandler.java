package net.pladema.clinichistory.requests.medicationrequests.infrastructure.input.rest.exception;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation.MedicationRequestValidationException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice(basePackages = "net.pladema.clinichistory.requests.medicationrequests")
public class MedicationRequestExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RuntimeException.class })
	public ApiErrorDto handleValidationExceptions(RuntimeException ex) {
		log.error("Constraint violation -> {}", ex.getMessage());
		return new ApiErrorDto("Constraint violation", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MedicationRequestValidationException.class })
	public ApiErrorDto handleMedicationRequestDigitalPrescriptionValidationException(MedicationRequestValidationException ex) {
		log.error("Constraint violation -> {}", ex.getMessage());
		return new ApiErrorDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(SnowstormPortException.class)
	public ApiErrorDto handleSnowstormPortException(SnowstormPortException ex) {
		log.error("Constraint violation -> {}", ex.getMessage());
		return new ApiErrorDto(ex.getCode().name(), ex.getMessage());
	}

}

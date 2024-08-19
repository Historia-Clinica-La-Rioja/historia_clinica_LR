package net.pladema.clinichistory.requests.servicerequests.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.clinichistory.requests.servicerequests")
public class ServiceRequestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class })
    public ApiErrorDto handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Constraint violation -> {}", ex.getMessage(), ex);
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RuntimeException.class })
	public ApiErrorDto handleRuntimeException(RuntimeException ex) {
		log.error("Constraint violation -> {}", ex.getMessage(), ex);
		return new ApiErrorDto("Constraint violation", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DiagnosticReportObservationException.class })
	public ApiErrorMessageDto handleDiagnosticReportObservation(DiagnosticReportObservationException ex) {
		log.error("DiagnosticReportObservationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode(), ex.getMessage(), ex.getParams());
	}

}

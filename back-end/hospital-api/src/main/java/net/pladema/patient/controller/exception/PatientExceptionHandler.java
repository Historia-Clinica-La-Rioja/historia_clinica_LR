package net.pladema.patient.controller.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

import net.pladema.patient.controller.service.exception.AuditPatientException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"net.pladema.patient"})
public class PatientExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({AuditPatientException.class})
	protected ApiErrorMessageDto handleAuditPatientException(AuditPatientException ex) {
		log.debug("AuditPatientException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

}

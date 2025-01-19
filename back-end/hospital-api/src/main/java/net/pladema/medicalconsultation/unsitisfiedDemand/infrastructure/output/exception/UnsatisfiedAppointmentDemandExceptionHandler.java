package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.unsitisfiedDemand.application.exception.UnsatisfiedAppointmentDemandException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.unsitisfiedDemand")
public class UnsatisfiedAppointmentDemandExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({UnsatisfiedAppointmentDemandException.class})
	protected ApiErrorMessageDto handleUnsatisfiedAppointmentDemandException(UnsatisfiedAppointmentDemandException ex) {
		log.debug("UnsatisfiedAppointmentDemandException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

}

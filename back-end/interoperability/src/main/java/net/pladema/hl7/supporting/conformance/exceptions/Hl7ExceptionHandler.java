package net.pladema.hl7.supporting.conformance.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.exceptions.DiagnosticReportException;
import net.pladema.hl7.dataexchange.exceptions.PrescriptionException;
import net.pladema.hl7.dataexchange.exceptions.DispenseValidationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.hl7")
@Slf4j
public class Hl7ExceptionHandler {

	@ExceptionHandler({ FhirClientException.class })
	protected ResponseEntity<ApiErrorMessageDto> handleFhirClientExceptionException(FhirClientException ex) {
		log.error("FhirClientException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage()), ex.getStatus());
	}

	@ExceptionHandler({ PrescriptionException.class })
	protected ResponseEntity<ApiErrorMessageDto> handlePrescriptionException(PrescriptionException ex) {
		log.error("PrescriptionException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode(), ex.getMessage()), ex.getStatus());
	}

	@ExceptionHandler({DispenseValidationException.class})
	protected ResponseEntity<ApiErrorMessageDto> handleDispenseValidationException(DispenseValidationException ex) {
		log.error("DispenseValidationException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode(), ex.getMessage()), ex.getStatus());
	}

	@ExceptionHandler({DiagnosticReportException.class})
	protected ResponseEntity<ApiErrorMessageDto> handleDiagnosticReportException(DiagnosticReportException ex) {
		log.error("DiagnosticReportException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode(), ex.getMessage()), ex.getStatus());
	}
}


package net.pladema.clinichistory.outpatient.createoutpatient.controller.exceptions;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions.HCICIE10Exception;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions.MarkAsErrorAProblemException;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientConsultationServiceRequestException;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.clinichistory.outpatient")
@RequiredArgsConstructor
public class OutpatientExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OutpatientExceptionHandler.class);

	private final MessageSource messageSource;

	private ApiErrorMessageDto buildErrorMessage(String error, Locale locale) {
		return new ApiErrorMessageDto(
				error,
				messageSource.getMessage(error, null, error, locale)
		);
	}

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ HCICIE10Exception.class })
	protected ApiErrorMessageDto handleHCICIE10Exception(HCICIE10Exception ex, Locale locale) {
		LOG.error("HCICIE10Exception exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ CreateOutpatientDocumentException.class })
	protected ApiErrorDto handleCreateOutpatientDocumentException(CreateOutpatientDocumentException ex, Locale locale) {
		LOG.error("CreateOutpatientDocumentException exception -> {}", ex.getMessage());
		return new ApiErrorDto(ex.getCode().toString(), ex.getMessages());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MarkAsErrorAProblemException.class })
	protected ApiErrorMessageDto handleMarkAsErrorAProblemException(MarkAsErrorAProblemException ex, Locale locale) {
		LOG.error("MarkAsErrorAProblemException exception -> {}", ex.getMessage());
		return buildErrorMessage(ex.getMessage(), locale);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ CreateOutpatientConsultationServiceRequestException.class })
	protected ApiErrorMessageDto handleCreateOutpatientConsultationServiceRequestException(CreateOutpatientConsultationServiceRequestException ex, Locale locale) {
		LOG.error("CreateOutpatientConsultationServiceRequestException exception -> {}", ex.getMessage());
		var msg = ex.getParams().entrySet().stream()
		.map(e -> String.format("%s %s", e.getKey(), e.getValue()))
		.collect(Collectors.joining(", "));
		return new ApiErrorMessageDto(ex.getCode().toString(), msg);
	}
}


package net.pladema.hl7.supporting.conformance.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
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
}


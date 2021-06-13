package net.pladema.clinichistory.documents.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.clinichistory.documents.core.cie10.exceptions.HCICIE10Exception;
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
@RestControllerAdvice(basePackages = "net.pladema.clinichistory.documents")
public class HciExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(HciExceptionHandler.class);

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ HCICIE10Exception.class })
	protected ApiErrorMessageDto handleHCICIE10Exception(HCICIE10Exception ex, Locale locale) {
		LOG.error("HCICIE10Exception exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


package net.pladema.clinichistory.outpatient.createoutpatient.controller.exceptions;

import net.pladema.clinichistory.documents.core.cie10.exceptions.HCICIE10Exception;
import net.pladema.snowstorm.controller.exceptions.dto.ApiSnowstormErrorMessageDto;
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
@RestControllerAdvice(basePackages = "net.pladema.clinichistory.outpatient")
public class OutpatientExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OutpatientExceptionHandler.class);

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ HCICIE10Exception.class })
	protected ApiSnowstormErrorMessageDto handleHCICIE10Exception(HCICIE10Exception ex, Locale locale) {
		LOG.error("HCICIE10Exception exception -> {}", ex.getMessage());
		return new ApiSnowstormErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


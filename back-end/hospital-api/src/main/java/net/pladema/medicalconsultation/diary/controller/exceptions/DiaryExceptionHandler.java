package net.pladema.medicalconsultation.diary.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
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
@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.diary")
public class DiaryExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ DiaryOpeningHoursException.class })
	protected ApiErrorMessageDto handleDiaryOpeningHoursException(DiaryOpeningHoursException ex, Locale locale) {
		LOG.debug("DiaryOpeningHoursException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}


}


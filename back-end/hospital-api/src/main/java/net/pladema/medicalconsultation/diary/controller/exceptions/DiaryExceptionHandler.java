package net.pladema.medicalconsultation.diary.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryAvailableAppointmentsException;
import net.pladema.medicalconsultation.diary.application.exceptions.DiaryBookingRestrictionException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryOpeningHoursException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.diary")
public class DiaryExceptionHandler {

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({ DiaryOpeningHoursException.class })
	protected ApiErrorMessageDto handleDiaryOpeningHoursException(DiaryOpeningHoursException ex, Locale locale) {
		log.debug("DiaryOpeningHoursException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DiaryException.class })
	protected ApiErrorMessageDto handleDiaryException(DiaryException ex, Locale locale) {
		log.debug("DiaryException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DiaryAvailableAppointmentsException.class })
	protected ApiErrorMessageDto handleDiaryAvailableAppointmentsException(DiaryAvailableAppointmentsException ex) {
		log.debug("DiaryAvailableAppointmentsException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DiaryBookingRestrictionException.class })
	protected ApiErrorMessageDto handleDiaryBookingRestrictionException(DiaryBookingRestrictionException ex) {
		log.debug("DiaryBookingRestrictionException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}


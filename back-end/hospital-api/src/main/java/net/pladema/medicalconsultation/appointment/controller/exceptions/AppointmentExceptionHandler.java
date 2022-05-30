package net.pladema.medicalconsultation.appointment.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.appointment")
@Slf4j
public class AppointmentExceptionHandler {

	@ExceptionHandler({ DiaryNotFoundException.class })
	protected ApiErrorMessageDto handleDiaryNotFoundException(DiaryNotFoundException ex, Locale locale) {
		log.debug("DiaryNotFoundException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}
}

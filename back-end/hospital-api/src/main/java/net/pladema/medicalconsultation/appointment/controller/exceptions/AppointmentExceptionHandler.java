package net.pladema.medicalconsultation.appointment.controller.exceptions;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateException;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentNotFoundException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ UpdateAppointmentDateException.class })
	protected ApiErrorMessageDto handleUpdateAppointmentDateException(UpdateAppointmentDateException ex, Locale locale) {
		log.debug("UpdateAppointmentDateException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ AppointmentNotFoundException.class })
	protected ApiErrorMessageDto handleAppointmentNotFoundException(AppointmentNotFoundException ex, Locale locale) {
		log.debug("AppointmentNotFoundException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode(), ex.getMessage());
	}
}


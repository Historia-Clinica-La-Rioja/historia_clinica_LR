package net.pladema.medicalconsultation.appointment.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentException;
import net.pladema.medicalconsultation.appointment.service.exceptions.NotifyPatientException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import net.pladema.medicalconsultation.appointment.service.impl.exceptions.RecurringAppointmentException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.appointment")
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
	@ExceptionHandler({ AppointmentException.class })
	protected ApiErrorMessageDto handleAppointmentNotFoundException(AppointmentException ex, Locale locale) {
		log.debug("AppointmentException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ NotifyPatientException.class })
	protected ApiErrorMessageDto handleNotifyPatientException(NotifyPatientException ex) {
		log.debug("NotifyPatientException exception -> {}", ex.getMessage());
		if (ex.sectorId == null) {
			return new ApiErrorMessageDto(
					"no-data",
					"No se pudo enviar la notificación"
			);
		}
		return new ApiErrorMessageDto(
				"no-topic",
				String.format("Falta configurar el sector %s", ex.sectorId),
				Map.of("sectorId", ex.sectorId)
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RecurringAppointmentException.class })
	protected ApiErrorMessageDto handleRecurringAppointmentOverturnException(RecurringAppointmentException ex, Locale locale) {
		log.debug("RecurringAppointmentOverturnException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null, ex.getMessage());
	}
}


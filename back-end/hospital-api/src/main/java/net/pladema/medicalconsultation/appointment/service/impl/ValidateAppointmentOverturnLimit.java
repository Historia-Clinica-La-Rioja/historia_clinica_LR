package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.RecurringAppointmentException;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursValidatorService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ValidateAppointmentOverturnLimit {

	private final DiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

	private final static String FUTURE_OVERTURN_ERROR = "Hay al menos una franja horaria que ya tiene los sobreturnos al límite";

	public void checkFutureAvailableOverturn(AppointmentBo newAppointment, LocalDate initDate) {
		if (newAppointment.isOverturn()) {
			boolean allowNewOverturn = diaryOpeningHoursValidatorService.allowNewOverturn(newAppointment.getDiaryId(), newAppointment.getOpeningHoursId(), initDate);
			if (!allowNewOverturn)
				throw new RecurringAppointmentException(FUTURE_OVERTURN_ERROR);
		}
	}
}

package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateEveryWeekAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CreateEveryWeekAppointmentServiceImpl implements CreateEveryWeekAppointmentService {

	private static final Short WEEK_DAYS = 7;

	private final CreateAppointmentService createAppointmentService;

	private final AppointmentService appointmentService;

	private final ValidateAppointmentOverturnLimit validateAppointmentOverturnLimit;

	@Transactional
	@Override
	public boolean execute(AppointmentBo newAppointment, LocalDate diaryEndDate) {
		log.debug("Input parameters -> newAppointment {}, diaryEndDate {}", newAppointment, diaryEndDate);

		Optional<AppointmentBo> currentAppointment = appointmentService.getAppointment(newAppointment.getId());

		if (currentAppointment.isPresent()) {
			if (newAppointment.getAppointmentOptionId() != null) {
				appointmentService.checkUpdateType(currentAppointment.get(), newAppointment);
			} else {
				for (LocalDate initDate = newAppointment.getDate().plusDays(WEEK_DAYS); !initDate.isAfter(diaryEndDate); initDate = initDate.plusDays(WEEK_DAYS)) {
					validateAppointmentOverturnLimit.checkFutureAvailableOverturn(newAppointment, initDate);
					newAppointment.setDate(initDate);
					newAppointment.setRecurringTypeBo(new RecurringTypeBo(RecurringAppointmentType.EVERY_WEEK.getId(), RecurringAppointmentType.EVERY_WEEK.getValue()));
					newAppointment.setParentAppointmentId(newAppointment.getId());
					createAppointmentService.execute(newAppointment);
				}
			}
			return true;
		}
		return false;
	}
}

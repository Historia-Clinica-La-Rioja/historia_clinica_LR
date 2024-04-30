package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.repository.CustomAppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.CustomAppointment;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateCustomAppointmentService;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.CreateCustomAppointmentBo;

import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CreateCustomAppointmentServiceImpl implements CreateCustomAppointmentService {

	private final CreateAppointmentService createAppointmentService;

	private static final Short WEEK_DAYS = 7;

	private final AppointmentService appointmentService;

	private final CustomAppointmentRepository customAppointmentRepository;

	private final ValidateAppointmentOverturnLimit validateAppointmentOverturnLimit;

	@Transactional
	@Override
	public boolean execute(CreateCustomAppointmentBo bo) {
		log.debug("Input parameters -> bo {}", bo);
		AppointmentBo appointmentBo = bo.getCreateAppointmentBo();
		CustomRecurringAppointmentBo customRecurringAppointmentBo = bo.getCustomRecurringAppointmentBo();
		Short repeatEvery = customRecurringAppointmentBo.getRepeatEvery();

		Optional<AppointmentBo> currentAppointment = appointmentService.getAppointment(appointmentBo.getId());

		if (currentAppointment.isPresent()) {
			if (appointmentBo.getAppointmentOptionId() != null) {
				appointmentService.updateAppointmentByOptionId(currentAppointment.get(), appointmentBo);
			} else {
				customAppointmentSave(currentAppointment.get().getId(), customRecurringAppointmentBo.getRepeatEvery(), customRecurringAppointmentBo.getWeekDayId(), customRecurringAppointmentBo.getEndDate());
				for (LocalDate initDate = appointmentBo.getDate().plusDays(WEEK_DAYS * repeatEvery); !initDate.isAfter(customRecurringAppointmentBo.getEndDate()); initDate = initDate.plusDays(WEEK_DAYS * repeatEvery)) {
					validateAppointmentOverturnLimit.checkFutureAvailableOverturn(bo.getCreateAppointmentBo(), initDate);
					appointmentBo.setDate(initDate);
					appointmentBo.setRecurringTypeBo(new RecurringTypeBo(RecurringAppointmentType.CUSTOM.getId(), RecurringAppointmentType.CUSTOM.getValue()));
					appointmentBo.setParentAppointmentId(appointmentBo.getId());
					Integer appointmentId = createAppointmentService.execute(appointmentBo).getId();
					customAppointmentSave(appointmentId, customRecurringAppointmentBo.getRepeatEvery(), customRecurringAppointmentBo.getWeekDayId(), customRecurringAppointmentBo.getEndDate());
					appointmentService.verifyRecurringAppointmentsOverturn(appointmentBo.getDiaryId());
				}
			}
			return true;
		}
		return false;
	}

	private void customAppointmentSave(Integer appointmentId, Short repeatEvery, Short weekDayId, LocalDate endDate) {
		customAppointmentRepository.save(
				new CustomAppointment(
						appointmentId,
						repeatEvery,
						weekDayId,
						endDate
				)
		);
	}
}

package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryDeleteableAppoinmentsValid;

@RequiredArgsConstructor
public class DiaryDeleteableAppointmentsValidator
		implements ConstraintValidator<DiaryDeleteableAppoinmentsValid, Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryDeleteableAppointmentsValidator.class);

	private final AppointmentService appointmentService;

	@Override
	public void initialize(DiaryDeleteableAppoinmentsValid constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(Integer diaryId, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> diaryId {}", diaryId);
		Collection<AppointmentBo> appointments = appointmentService.getAppointmentsByDiaries(Arrays.asList(diaryId), null, null);

		Optional<AppointmentBo> apmtActive = appointments.stream()
				.filter(apmt -> !apmt.getAppointmentStateId().equals(AppointmentState.CANCELLED) && !apmt.getAppointmentStateId().equals(AppointmentState.BLOCKED)).findFirst();

		if (apmtActive.isPresent()) {
			buildResponse(context, "{diary.appointments.invalid.cancelled}");
			return false;
		}
		return true;
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

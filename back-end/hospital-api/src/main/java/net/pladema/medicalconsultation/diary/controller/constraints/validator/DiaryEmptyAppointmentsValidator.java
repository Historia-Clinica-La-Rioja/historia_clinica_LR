package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import static java.util.stream.Collectors.groupingBy;
import static net.pladema.sgx.dates.utils.DateUtils.getWeekDay;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;

@RequiredArgsConstructor
public class DiaryEmptyAppointmentsValidator implements ConstraintValidator<DiaryEmptyAppointmentsValid, DiaryDto> {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryEmptyAppointmentsValidator.class);

	private final AppointmentService appointmentService;

	private final LocalDateMapper localDateMapper;

	@Override
	public void initialize(DiaryEmptyAppointmentsValid constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(DiaryDto diaryToUpdate, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> diaryToUpdateId {}", diaryToUpdate);
		Collection<AppointmentBo> appointments = appointmentService.getFutureActiveAppointmentsByDiary(diaryToUpdate.getId());

		HashMap<Short, List<DiaryOpeningHoursDto>> appointmentsByWeekday = diaryToUpdate.getDiaryOpeningHours().stream()
				.collect(groupingBy(doh -> doh.getOpeningHours().getDayWeekId(),
						HashMap<Short, List<DiaryOpeningHoursDto>>::new, Collectors.toList()));

		Optional<AppointmentBo> appointmentOutOfBounds = appointments.stream().filter(a -> {
			List<DiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
			return newHours == null
					|| newHours.stream().noneMatch(newOH -> isValidAppointment(a, newOH.getOpeningHours()));
		}).findFirst();

		if (appointmentOutOfBounds.isPresent()) {
			buildResponse(context, "{diary.appointments.invalid}");
			return false;
		}
		return true;
	}

	private boolean isValidAppointment(AppointmentBo appointment, OpeningHoursDto openingHours) {
		LocalTime from = localDateMapper.fromStringToLocalTime(openingHours.getFrom());
		LocalTime to = localDateMapper.fromStringToLocalTime(openingHours.getTo());
		return (appointment.getHour().equals(from) || appointment.getHour().isAfter(from)) && appointment.getHour().isBefore(to);
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

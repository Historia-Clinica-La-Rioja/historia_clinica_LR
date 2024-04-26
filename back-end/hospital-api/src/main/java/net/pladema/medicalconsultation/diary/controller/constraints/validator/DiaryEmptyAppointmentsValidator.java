package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import static java.util.stream.Collectors.groupingBy;
import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@RequiredArgsConstructor
public class DiaryEmptyAppointmentsValidator implements ConstraintValidator<DiaryEmptyAppointmentsValid, DiaryDto> {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryEmptyAppointmentsValidator.class);

	private final AppointmentService appointmentService;

	private final LocalDateMapper localDateMapper;

	private final FeatureFlagsService featureFlagsService;

	private final DiaryService diaryService;

	private final DiaryOpeningHoursService diaryOpeningHoursService;

	@Override
	public void initialize(DiaryEmptyAppointmentsValid constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(DiaryDto diaryToUpdate, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> diaryToUpdateId {}", diaryToUpdate);
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_AGENDA_DINAMICA))
			return validateAppointments(diaryToUpdate, context);
		return true;
	}

	private boolean validateAppointments(DiaryDto diaryToUpdate, ConstraintValidatorContext context) {
		DiaryBo diaryBo = diaryService.getDiaryById(diaryToUpdate.getId());
		boolean hasChangeInDateBounds = hasChangeInDateBounds(diaryToUpdate, diaryBo);
		boolean hasChangeInOpeningHours = hasChangeInOpeningHours(diaryToUpdate);

		if (!hasChangeInDateBounds && ! hasChangeInOpeningHours)
			return true;

		LocalDate newStartDate = localDateMapper.fromStringToLocalDate(diaryToUpdate.getStartDate());
		LocalDate newEndDate = localDateMapper.fromStringToLocalDate(diaryToUpdate.getEndDate());

		Collection<AppointmentBo> appointments = appointmentService.getAppointmentsByDiaries(List.of(diaryToUpdate.getId()), null, null).stream()
				.filter(a -> !a.getAppointmentStateId().equals(AppointmentState.BLOCKED)).collect(Collectors.toList());

		HashMap<Short, List<DiaryOpeningHoursDto>> appointmentsByWeekday = diaryToUpdate.getDiaryOpeningHours().stream()
				.collect(groupingBy(doh -> doh.getOpeningHours().getDayWeekId(),
						HashMap<Short, List<DiaryOpeningHoursDto>>::new, Collectors.toList()));

		Optional<AppointmentBo> appointmentOutOfDiaryBounds = Optional.empty();

		if (hasChangeInDateBounds)
			appointmentOutOfDiaryBounds = appointments.stream().filter(a -> isAppointmentOutOfNewDiaryBounds(a, newStartDate, newEndDate, diaryBo)).findFirst();

		if (appointmentOutOfDiaryBounds.isPresent())
			return parseErrorResponse(context, "{diary.appointments.invalid}");

		if (hasChangeInOpeningHours) {
			Optional<AppointmentBo> appointmentOutOfBoundsOpeningHours = appointments.stream().filter(a -> filterOutOfBoundsOpeningHours(a, appointmentsByWeekday, newStartDate, newEndDate)).findFirst();

			if (appointmentOutOfBoundsOpeningHours.isPresent())
				return parseErrorResponse(context, "{diary.appointments.invalid}");

			Optional<AppointmentBo> appointmentWithDifferentTypeOfMedicalAttention = appointments.stream().filter(a -> filterOpeningHours(a, appointmentsByWeekday)).findFirst();

			if (appointmentWithDifferentTypeOfMedicalAttention.isPresent())
				return parseErrorResponse(context, "{diary.appointments.invalid.type}");
		}

		return true;
	}

	private boolean parseErrorResponse(ConstraintValidatorContext context, String message) {
		buildResponse(context, message);
		return false;
	}

	private boolean isAppointmentOutOfNewDiaryBounds(AppointmentBo appointmentBo, LocalDate newStartDate, LocalDate newEndDate, DiaryBo diaryBo){
		return (newStartDate.isAfter(diaryBo.getStartDate()) && appointmentBo.getDate().isBefore(newStartDate) && !appointmentBo.getDate().isBefore(diaryBo.getStartDate())) ||
				(newEndDate.isBefore(diaryBo.getEndDate()) && appointmentBo.getDate().isAfter(newEndDate) && !appointmentBo.getDate().isAfter(diaryBo.getEndDate()));
	}
	private boolean filterOutOfBoundsOpeningHours(AppointmentBo a, HashMap<Short, List<DiaryOpeningHoursDto>> appointmentsByWeekday, LocalDate from, LocalDate to) {
		List<DiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
		return newHours == null || outOfDiaryBounds(from, to, a) || outOfOpeningHoursBounds(a, newHours);
	}

	private boolean filterOpeningHours(AppointmentBo a, HashMap<Short, List<DiaryOpeningHoursDto>> appointmentsByWeekday) {
		List<DiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
		return newHours == null || differentTypeOfMedicalAttention(a, newHours);
	}

	private boolean outOfOpeningHoursBounds(AppointmentBo a, List<DiaryOpeningHoursDto> newHours) {
		return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()));
	}

	private boolean differentTypeOfMedicalAttention (AppointmentBo a, List<DiaryOpeningHoursDto> newHours) {
		return newHours.stream().noneMatch(newOH -> sameMedicalAttention(a, newOH));
	}

	private boolean sameMedicalAttention(AppointmentBo a, DiaryOpeningHoursDto newOH) {
		return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
	}

	private boolean outOfDiaryBounds(LocalDate from, LocalDate to, AppointmentBo a) {
		return !isBetween(from, to, a);
	}

	private boolean isBetween(LocalDate from, LocalDate to, AppointmentBo a) {
		return !a.getDate().isBefore(from) && !a.getDate().isAfter(to);
	}

	private boolean fitsIn(AppointmentBo appointment, OpeningHoursDto openingHours) {
		LocalTime from = localDateMapper.fromStringToLocalTime(openingHours.getFrom());
		LocalTime to = localDateMapper.fromStringToLocalTime(openingHours.getTo());
		return (appointment.getHour().equals(from) || appointment.getHour().isAfter(from)) && appointment.getHour().isBefore(to);
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

	private boolean hasChangeInDateBounds(DiaryDto diaryToUpdate, DiaryBo diaryBo) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(diaryToUpdate.getStartDate());
		LocalDate endDate = localDateMapper.fromStringToLocalDate(diaryToUpdate.getEndDate());
		return (startDate.isAfter(diaryBo.getStartDate()) || endDate.isBefore(diaryBo.getEndDate()));
	}

	private boolean hasChangeInOpeningHours(DiaryDto diaryToUpdate) {
		boolean hasChange = false;
		List<DiaryOpeningHoursBo> diaryOpeningHoursBos = new ArrayList<>(diaryOpeningHoursService.getDiaryOpeningHours(diaryToUpdate.getId()));
		List<DiaryOpeningHoursDto> diaryOpeningHoursDtos = diaryToUpdate.getDiaryOpeningHours();
		for (DiaryOpeningHoursDto diaryOpeningHoursDto: diaryOpeningHoursDtos){
			OpeningHoursDto ohDto = diaryOpeningHoursDto.getOpeningHours();
			OpeningHoursBo ohBo = mapToOpeningHoursBo(ohDto);
			if (hasChangeInOpeningHours(diaryOpeningHoursBos, ohBo, diaryOpeningHoursDto.getMedicalAttentionTypeId())) {
				hasChange = true;
			}
		}
		return hasChange;
	}

	private boolean hasChangeInOpeningHours(List<DiaryOpeningHoursBo> diaryOpeningHoursBos, OpeningHoursBo openingHoursBo, Short medicalAttentionTypeId){
		return diaryOpeningHoursBos.stream()
				.anyMatch(dohBo ->!dohBo.getMedicalAttentionTypeId().equals(medicalAttentionTypeId) && dohBo.getOpeningHours().equals(openingHoursBo))
				||!diaryOpeningHoursBos.stream().map(DiaryOpeningHoursBo::getOpeningHours).collect(Collectors.toList()).contains(openingHoursBo);
	}

	private OpeningHoursBo mapToOpeningHoursBo (OpeningHoursDto openingHoursDto){
		OpeningHoursBo openingHoursBo = new OpeningHoursBo();
		openingHoursBo.setDayWeekId(openingHoursDto.getDayWeekId());
		openingHoursBo.setFrom(localDateMapper.fromStringToLocalTime(openingHoursDto.getFrom()));
		openingHoursBo.setTo(localDateMapper.fromStringToLocalTime(openingHoursDto.getTo()));
		return openingHoursBo;
	}

}

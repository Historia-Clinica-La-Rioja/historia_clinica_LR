package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryEmptyAppointmentsValidator;

import net.pladema.medicalconsultation.equipmentdiary.application.port.output.EquipmentDiaryPort;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryEmptyAppointmentsValid;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryOpeningHoursDto;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentOpeningHoursDto;

import net.pladema.medicalconsultation.equipmentdiary.domain.UpdateEquipmentDiaryAppointmentBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Slf4j
public class EquipmentDiaryEmptyAppointmentsValidator implements ConstraintValidator<EquipmentDiaryEmptyAppointmentsValid, EquipmentDiaryDto> {

	private static final Logger LOG = LoggerFactory.getLogger(DiaryEmptyAppointmentsValidator.class);

	private final LocalDateMapper localDateMapper;

	private final EquipmentDiaryPort equipmentDiaryPort;

	@Override
	public void initialize(EquipmentDiaryEmptyAppointmentsValid constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(EquipmentDiaryDto equipmentDiaryToUpdate, ConstraintValidatorContext context) {
		LOG.debug("Input parameters -> equipmentDiaryToUpdate {}", equipmentDiaryToUpdate);
		List<UpdateEquipmentDiaryAppointmentBo> appointments = equipmentDiaryPort.getUpdateEquipmentDiaryAppointments(equipmentDiaryToUpdate.getId());

		LocalDate from = localDateMapper.fromStringToLocalDate(equipmentDiaryToUpdate.getStartDate());
		LocalDate to = localDateMapper.fromStringToLocalDate(equipmentDiaryToUpdate.getEndDate());

		HashMap<Short, List<EquipmentDiaryOpeningHoursDto>> appointmentsByWeekday = equipmentDiaryToUpdate.getEquipmentDiaryOpeningHours().stream()
				.collect(groupingBy(edoh -> edoh.getOpeningHours().getDayWeekId(),
						HashMap<Short, List<EquipmentDiaryOpeningHoursDto>>::new, Collectors.toList()));

		Optional<UpdateEquipmentDiaryAppointmentBo> appointmentOutOfBounds = appointments.stream().filter(a -> {
			List<EquipmentDiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
			return newHours == null
					|| outOfDiaryBounds(from, to, a)
					|| outOfOpeningHoursBounds(a, newHours);
		}).findFirst();

		if (appointmentOutOfBounds.isPresent()) {
			buildResponse(context, "{diary.appointments.invalid}");
			return false;
		}
		return true;
	}

	private boolean outOfOpeningHoursBounds(UpdateEquipmentDiaryAppointmentBo a, List<EquipmentDiaryOpeningHoursDto> newHours) {
		return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()) && sameMedicalAttention(a, newOH));
	}

	private boolean sameMedicalAttention(UpdateEquipmentDiaryAppointmentBo a, EquipmentDiaryOpeningHoursDto newOH) {
		return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
	}

	private boolean outOfDiaryBounds(LocalDate from, LocalDate to, UpdateEquipmentDiaryAppointmentBo a) {
		return !isBetween(from, to, a);
	}

	private boolean isBetween(LocalDate from, LocalDate to, UpdateEquipmentDiaryAppointmentBo a) {
		return a.getDate().compareTo(from)>=0 && a.getDate().compareTo(to)<=0;
	}

	private boolean fitsIn(UpdateEquipmentDiaryAppointmentBo appointment, EquipmentOpeningHoursDto openingHours) {
		LocalTime from = localDateMapper.fromStringToLocalTime(openingHours.getFrom());
		LocalTime to = localDateMapper.fromStringToLocalTime(openingHours.getTo());
		return (appointment.getTime().equals(from) || appointment.getTime().isAfter(from)) && appointment.getTime().isBefore(to);
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

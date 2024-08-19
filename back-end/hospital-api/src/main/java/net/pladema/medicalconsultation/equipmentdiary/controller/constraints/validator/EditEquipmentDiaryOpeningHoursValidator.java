package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EditEquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmentDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EditEquipmentDiaryOpeningHoursValidator implements ConstraintValidator<EditEquipmentDiaryOpeningHoursValid, EquipmentDiaryDto> {

	private static final String OUTPUT = "Output -> {}";

	private final EquipmentDiaryOpeningHoursValidatorService equipmentDiaryOpeningHoursValidatorService;

	private final EquipmentDiaryMapper equipmentDiaryMapper;

	@Override
	public void initialize(EditEquipmentDiaryOpeningHoursValid constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(EquipmentDiaryDto equipmentDiaryDto, ConstraintValidatorContext context) {
		log.debug("Input parameters -> equipmentDiaryDto {}", equipmentDiaryDto);

		if(! overturnValuesAreValid(equipmentDiaryDto, context)) {
			log.debug(OUTPUT, Boolean.FALSE);
			return false;
		}

		EquipmentDiaryBo equipmentDiaryBo = equipmentDiaryMapper.toEquipmentDiaryBo(equipmentDiaryDto);
		equipmentDiaryBo.setId(equipmentDiaryDto.getId());

		Comparator<EquipmentDiaryOpeningHoursBo> weekDayOrder = Comparator
				.comparing(edoh -> edoh.getOpeningHours().getDayWeekId(), Short::compareTo);
		Comparator<EquipmentDiaryOpeningHoursBo> timeOrder = Comparator
				.comparing(edoh -> edoh.getOpeningHours().getFrom(), LocalTime::compareTo);

		List<EquipmentDiaryOpeningHoursBo> openingHours = equipmentDiaryBo.getDiaryOpeningHours();
		openingHours.sort(weekDayOrder.thenComparing(timeOrder));

		if (equipmentDiaryBo.getDiaryOpeningHours().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.no-opening-hours}")
					.addConstraintViolation();
			log.debug(OUTPUT, Boolean.FALSE);
			return Boolean.FALSE;
		}

		boolean overlap = equipmentDiaryOpeningHoursValidatorService.overlapDiaryOpeningHours(equipmentDiaryBo, openingHours);

		if (overlap) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overlap}")
					.addConstraintViolation();
			log.debug(OUTPUT, Boolean.FALSE);
			return false;
		}
		log.debug(OUTPUT, Boolean.TRUE);
		return true;
	}

	private boolean overturnValuesAreValid(EquipmentDiaryDto dto, ConstraintValidatorContext context){

		var isInvalidOverturnCount = dto.getEquipmentDiaryOpeningHours().stream()
				.anyMatch(diaryOpeningHoursDto ->
						diaryOpeningHoursDto.getOverturnCount() != null
								&& diaryOpeningHoursDto.getOverturnCount() < 0
				);
		if(isInvalidOverturnCount) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overturn-value-negative}")
					.addConstraintViolation();
			return false;
		}

		var isInvalidMedicalAttentionType = dto.getEquipmentDiaryOpeningHours().stream()
				.anyMatch(diaryOpeningHoursDto ->
						diaryOpeningHoursDto.getOverturnCount() != null
								&& diaryOpeningHoursDto.getOverturnCount() > 0
								&& diaryOpeningHoursDto.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS));
		if(isInvalidMedicalAttentionType) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{diary.attention.invalid.spontaneous-with-overturn}")
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}

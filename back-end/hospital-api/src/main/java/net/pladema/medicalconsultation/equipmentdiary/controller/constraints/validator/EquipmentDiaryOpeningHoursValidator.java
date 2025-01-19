package net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;

import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmentDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursValidatorService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EquipmentDiaryOpeningHoursValidator implements ConstraintValidator<EquipmentDiaryOpeningHoursValid, EquipmentDiaryADto> {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryOpeningHoursValidator.class);

    private static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursValidatorService diaryOpeningHoursValidatorService;

    private final EquipmentDiaryMapper diaryMapper;

    @Override
    public void initialize(EquipmentDiaryOpeningHoursValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(EquipmentDiaryADto diaryADto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        EquipmentDiaryBo diaryBo = diaryMapper.toEquipmentDiaryBo(diaryADto);

        Comparator<EquipmentDiaryOpeningHoursBo> weekDayOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getDayWeekId(), Short::compareTo);
        Comparator<EquipmentDiaryOpeningHoursBo> timeOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getFrom(), LocalTime::compareTo);

        List<EquipmentDiaryOpeningHoursBo> openingHours = diaryBo.getDiaryOpeningHours();
        openingHours.sort(weekDayOrder.thenComparing(timeOrder));

        if (diaryBo.getDiaryOpeningHours().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{diary.attention.no-opening-hours}")
                    .addConstraintViolation();
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }

        boolean overlap = diaryOpeningHoursValidatorService.overlapDiaryOpeningHours(diaryBo, openingHours);

        if (overlap) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overlap}")
                    .addConstraintViolation();
            LOG.debug(OUTPUT, Boolean.FALSE);
            return Boolean.FALSE;
        }
        LOG.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }


}

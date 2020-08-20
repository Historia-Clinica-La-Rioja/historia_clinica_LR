package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pladema.medicalconsultation.diary.controller.constraints.DiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;

@RequiredArgsConstructor
public class DiaryOpeningHoursValidator implements ConstraintValidator<DiaryOpeningHoursValid, DiaryADto> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursValidator.class);

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final DiaryMapper diaryMapper;

    @Override
    public void initialize(DiaryOpeningHoursValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(DiaryADto diaryADto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);

        Comparator<DiaryOpeningHoursDto> weekDayOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getDayWeekId(), Short::compareTo);
        Comparator<DiaryOpeningHoursDto> timeOrder = Comparator
                .comparing(doh -> doh.getOpeningHours().getFrom(), String::compareTo);

        List<DiaryOpeningHoursDto> openingHours = diaryADto.getDiaryOpeningHours();
        openingHours.sort(weekDayOrder.thenComparing(timeOrder));

        boolean overlap = false;
        int index = 0;
        DiaryOpeningHoursDto last = openingHours.get(index++);
        DiaryOpeningHoursDto current;

        DiaryOpeningHoursBo diaryOpeningHoursBo = diaryMapper.toDiaryOpeningHoursBo(last);
        overlap = diaryOpeningHoursService.overlapWithOthers(diaryADto.getDoctorsOfficeId(), diaryOpeningHoursBo);
        while (index < openingHours.size() && !overlap) {
            current = openingHours.get(index++);
            overlap = last.overlap(current) || diaryOpeningHoursService.overlapWithOthers(diaryADto.getDoctorsOfficeId(), diaryOpeningHoursBo);
            last = current;
            diaryOpeningHoursBo = diaryMapper.toDiaryOpeningHoursBo(last);
        }



       if(overlap){
           context.disableDefaultConstraintViolation();
           context.buildConstraintViolationWithTemplate("{diary.attention.invalid.overlap}")
                   .addConstraintViolation();
            return false;
        }
        return true;
    }
}

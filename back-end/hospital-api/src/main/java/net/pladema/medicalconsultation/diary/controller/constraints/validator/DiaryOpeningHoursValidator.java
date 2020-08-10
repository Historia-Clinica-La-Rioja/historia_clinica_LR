package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import net.pladema.medicalconsultation.diary.controller.constraints.DiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.Comparator;
import java.util.List;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DiaryOpeningHoursValidator implements ConstraintValidator<DiaryOpeningHoursValid, DiaryADto> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryOpeningHoursValidator.class);

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

        while (index < openingHours.size() && !overlap) {
            current = openingHours.get(index++);
            overlap = last.overlap(current);
            last = current;
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

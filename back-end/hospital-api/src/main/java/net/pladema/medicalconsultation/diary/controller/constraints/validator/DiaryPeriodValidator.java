package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import net.pladema.medicalconsultation.diary.controller.constraints.DiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;

public class DiaryPeriodValidator implements ConstraintValidator<DiaryPeriodValid, DiaryADto> {

    private static final Logger LOG = LoggerFactory.getLogger(DiaryPeriodValidator.class);

    private final LocalDateMapper localDateMapper;

    private final DiaryService diaryService;

    public DiaryPeriodValidator(LocalDateMapper localDateMapper,
                                DiaryService diaryService){
        super();
        this.localDateMapper = localDateMapper;
        this.diaryService = diaryService;
    }

    @Override
    public void initialize(DiaryPeriodValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(DiaryADto diaryADto, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> diaryADto {}", diaryADto);
        LocalDate startDate = localDateMapper.fromStringToLocalDate(diaryADto.getStartDate());
        LocalDate endDate = localDateMapper.fromStringToLocalDate(diaryADto.getEndDate());

        if(endDate.isBefore(startDate)) {
            buildResponse(context, "{diary.period.invalid.range}");
            return false;
        }

        if(startDate.getYear() != endDate.getYear()) {
            buildResponse(context, "{diary.period.invalid.year}");
            return false;
        }

        List<Integer> overlappingDiary = diaryService
                .getAllOverlappingDiary(diaryADto.getHealthcareProfessionalId(),
                        diaryADto.getDoctorsOfficeId(), startDate, endDate);
        if(!overlappingDiary.isEmpty()){
            buildResponse(context, "{diary.period.invalid.overlap}");
            return false;
        }
        return true;
    }

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}

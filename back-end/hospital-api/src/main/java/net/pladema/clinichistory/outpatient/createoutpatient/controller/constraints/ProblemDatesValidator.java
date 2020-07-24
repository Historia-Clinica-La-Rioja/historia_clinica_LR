package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientProblemDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ProblemDatesValidator implements ConstraintValidator<ProblemDates, OutpatientProblemDto> {

    private static final Logger LOG = LoggerFactory.getLogger(ProblemDatesValidator.class);

    private final LocalDateMapper localDateMapper;

    public ProblemDatesValidator(LocalDateMapper localDateMapper) {
        this.localDateMapper = localDateMapper;
    }

    @Override
    public void initialize(ProblemDates constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(OutpatientProblemDto problemDto, ConstraintValidatorContext context) {
       LOG.debug("Input parameters -> problemDto {}", problemDto);
       if (problemDto.getStartDate() == null)
           return false;
       if (problemDto.getEndDate() == null)
           return true;
       LocalDate endDate = localDateMapper.fromStringToLocalDate(problemDto.getEndDate());
       LocalDate startDate = localDateMapper.fromStringToLocalDate(problemDto.getStartDate());
       return endDate.isEqual(startDate) || endDate.isAfter(startDate);
    }
}

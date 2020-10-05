package net.pladema.sgx.dates.controller.constraints.validator;

import net.pladema.sgx.dates.controller.constraints.DateDtoValid;
import net.pladema.sgx.dates.controller.dto.DateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateDtoValidator implements ConstraintValidator<DateDtoValid, DateDto> {

    public static final String DAY_PROPERTY = "day";
    public static final String MSG_KEY = "{date-dto.invalid}";

    @Override
    public void initialize(DateDtoValid constraintAnnotation) {
        //nothing to do
    }

    @Override
    public boolean isValid(DateDto dateDto, ConstraintValidatorContext constraintValidatorContext) {
        if (isDayOfMonthValid(dateDto))
            return true;
        setResponse(constraintValidatorContext, MSG_KEY, DAY_PROPERTY);
        return false;
    }

    private boolean isDayOfMonthValid(DateDto dateDto) {
        return dateDto.getDay() <= maxDayOfMonth(dateDto.getMonth(), dateDto.getYear());
    }

    private Integer maxDayOfMonth(Integer month, Integer year) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.lengthOfMonth();
    }

    private void setResponse(ConstraintValidatorContext constraintValidatorContext,
                             String message,
                             String propertyName) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
}

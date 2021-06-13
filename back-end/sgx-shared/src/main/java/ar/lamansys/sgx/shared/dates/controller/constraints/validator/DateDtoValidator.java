package ar.lamansys.sgx.shared.dates.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.controller.constraints.DateDtoValid;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateDtoValidator implements ConstraintValidator<DateDtoValid, DateDto> {

    public static final String DAY_PROPERTY = "day";
    public static final String MONTH_PROPERTY = "month";
    public static final String YEAR_PROPERTY = "year";

    public static final String INVALID_DATE_MSG_KEY = "{date-dto.invalid}";
    public static final String INVALID_DAY_MSG_KEY = "{date-dto.day.invalid}";
    public static final String INVALID_MONTH_MSG_KEY = "{date-dto.month.invalid}";
    public static final String INVALID_YEAR_MSG_KEY = "{date-dto.year.invalid}";
    public static final String NOT_NULL_VIOLATION_MSG_KEY = "{date-dto.not-null}";

    public static final int MIN_DAY_OF_MONTH = 1;
    public static final int MIN_YEAR = 0;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MAX_DAY_OF_MONTH_DEFAULT = 31;

    @Override
    public void initialize(DateDtoValid constraintAnnotation) {
        //nothing to do
    }

    @Override
    public boolean isValid(DateDto dateDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        if (dateDto.getDay() == null) {
            setResponse(constraintValidatorContext, NOT_NULL_VIOLATION_MSG_KEY, DAY_PROPERTY);
            valid = false;
        }

        if (dateDto.getMonth() == null ) {
            setResponse(constraintValidatorContext, NOT_NULL_VIOLATION_MSG_KEY, MONTH_PROPERTY);
            valid = false;
        }

        if (dateDto.getYear() == null) {
            setResponse(constraintValidatorContext, NOT_NULL_VIOLATION_MSG_KEY, YEAR_PROPERTY);
            valid = false;
        }

        if (dateDto.getYear() != null && dateDto.getYear() < MIN_YEAR) {
            setResponse(constraintValidatorContext, INVALID_YEAR_MSG_KEY, YEAR_PROPERTY);
            valid = false;
        }

        if (dateDto.getMonth() != null && !validMonth(dateDto.getMonth())) {
            setResponse(constraintValidatorContext, INVALID_MONTH_MSG_KEY, MONTH_PROPERTY);
            valid = false;
        }

        if (dateDto.getDay() != null && !validDay(dateDto)) {
            setResponse(constraintValidatorContext, INVALID_DAY_MSG_KEY, DAY_PROPERTY);
            valid = false;
        }

        if (!valid) {
            return false;
        }

        if (!isDayOfMonthValid(dateDto)) {
            setResponse(constraintValidatorContext, INVALID_DATE_MSG_KEY, DAY_PROPERTY);
            valid = false;
        }

        return valid;
    }

    private boolean validDay(DateDto dateDto) {
        return dateDto.getDay() >= MIN_DAY_OF_MONTH && dateDto.getDay() <= MAX_DAY_OF_MONTH_DEFAULT;
    }

    private boolean validMonth(Integer month) {
        return month >= MIN_MONTH && month <= MAX_MONTH;
    }

    private boolean isDayOfMonthValid(DateDto dateDto) {
        Integer day = dateDto.getDay();

        Integer maxDayOfMonth = maxDayOfMonth(dateDto.getMonth(), dateDto.getYear());
        return ((day >= MIN_DAY_OF_MONTH) && (day <= maxDayOfMonth));
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

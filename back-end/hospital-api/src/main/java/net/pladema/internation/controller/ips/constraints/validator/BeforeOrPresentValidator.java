package net.pladema.internation.controller.ips.constraints.validator;

import net.pladema.dates.configuration.JacksonDateFormatConfig;
import net.pladema.internation.controller.ips.constraints.BeforeOrPresent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BeforeOrPresentValidator implements ConstraintValidator<BeforeOrPresent, String> {

    private static final Logger LOG = LoggerFactory.getLogger(BeforeOrPresentValidator.class);

    @Override
    public void initialize(BeforeOrPresent constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(String datetime, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> datetime {}", datetime);
        if (datetime == null)
            return false;
        LocalDateTime date = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
        return !date.isAfter(LocalDateTime.now());
    }
}

package net.pladema.patient.controller.constraints.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pladema.patient.controller.constraints.FilterValid;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class FilterValidator implements ConstraintValidator<FilterValid, String> {

    private final ObjectMapper jackson;

    private static final Logger LOG = LoggerFactory.getLogger(FilterValidator.class);

    public FilterValidator(ObjectMapper jackson) {
        this.jackson = jackson;
    }

    @Override
    public void initialize(FilterValid constraintAnnotation) {
        //nothing to do
    }

    @Override
    public boolean isValid(String searchFilterStr, ConstraintValidatorContext constraintValidatorContext) {
        PatientSearchFilter searchFilter = null;
        try {
            searchFilter = jackson.readValue(searchFilterStr, PatientSearchFilter.class);
        } catch (IOException e) {
            LOG.error(String.format("Error mappeando filter: %s", searchFilterStr), e);
        }
        return (searchFilter != null && searchFilter.getFirstName() != null && searchFilter.getLastName() != null
                && searchFilter.getGenderId() != null && searchFilter.getIdentificationNumber() != null);
    }
}

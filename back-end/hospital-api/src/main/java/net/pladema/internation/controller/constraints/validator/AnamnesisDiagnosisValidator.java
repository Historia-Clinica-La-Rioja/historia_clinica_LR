package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.AnamnesisDiagnosisValid;
import net.pladema.internation.controller.dto.core.AnamnesisDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AnamnesisDiagnosisValidator implements ConstraintValidator<AnamnesisDiagnosisValid, Object[]> {

    private static final String DIAGNOSIS_PROPERTY = "anamnesisDto.diagnosis";

    @Override
    public void initialize(AnamnesisDiagnosisValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        AnamnesisDto anamnesis = (AnamnesisDto) parameters[2];
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{diagnosis.mandatory}")
                .addPropertyNode(DIAGNOSIS_PROPERTY)
                .addConstraintViolation();

        return anamnesis.getDiagnosis() != null && !anamnesis.getDiagnosis().isEmpty();
    }
}

package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.AnamnesisMainDiagnosisValid;
import net.pladema.internation.controller.documents.anamnesis.dto.AnamnesisDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AnamnesisMainDiagnosisValidator implements ConstraintValidator<AnamnesisMainDiagnosisValid, Object[]> {

    private static final String DIAGNOSIS_PROPERTY = "anamnesisDto.diagnosis";

    @Override
    public void initialize(AnamnesisMainDiagnosisValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        AnamnesisDto anamnesis = (AnamnesisDto) parameters[2];
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{diagnosis.main.repeated}")
                .addPropertyNode(DIAGNOSIS_PROPERTY)
                .addConstraintViolation();

        if (anamnesis.getDiagnosis() == null || anamnesis.getDiagnosis().isEmpty() || anamnesis.getMainDiagnosis() == null)
            return true;
        return anamnesis.getDiagnosis().stream().noneMatch(d -> d.getSnomed().equals(anamnesis.getMainDiagnosis().getSnomed()));
    }
}

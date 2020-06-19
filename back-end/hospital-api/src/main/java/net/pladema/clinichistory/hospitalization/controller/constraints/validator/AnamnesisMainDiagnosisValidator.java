package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.AnamnesisMainDiagnosisValid;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AnamnesisMainDiagnosisValidator implements ConstraintValidator<AnamnesisMainDiagnosisValid, Object[]> {

    private static final String DIAGNOSIS_PROPERTY = "anamnesisDto.diagnosis";

    @Override
    public void initialize(AnamnesisMainDiagnosisValid constraintAnnotation) {
        //empty until done
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {

        final AnamnesisDto anamnesis;
        for (Object p : parameters) {
            if (p instanceof AnamnesisDto) {
                anamnesis = (AnamnesisDto) p;

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{diagnosis.main.repeated}")
                        .addPropertyNode(DIAGNOSIS_PROPERTY)
                        .addConstraintViolation();

                if (anamnesis.getDiagnosis() == null || anamnesis.getDiagnosis().isEmpty() || anamnesis.getMainDiagnosis() == null)
                    return true;
                return anamnesis.getDiagnosis().stream().noneMatch(d -> d.getSnomed().equals(anamnesis.getMainDiagnosis().getSnomed()));
            }
        }
        return false;
    }
}
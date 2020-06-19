package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.constraints.AnamnesisValid;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;


@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class AnamnesisValidator implements ConstraintValidator<AnamnesisValid, Object[]> {

    private final InternmentEpisodeService internmentEpisodeService;

    public AnamnesisValidator(InternmentEpisodeService internmentEpisodeService) {
        this.internmentEpisodeService = internmentEpisodeService;
    }

    @Override
    public void initialize(AnamnesisValid constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer internmentEpisodeId = (Integer) parameters[1];
        return !internmentEpisodeService.haveAnamnesis(internmentEpisodeId);
    }
}

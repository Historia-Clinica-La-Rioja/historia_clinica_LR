package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;


@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class EvolutionNoteValidator implements ConstraintValidator<EvolutionNoteValid, Object[]> {

    private final InternmentEpisodeService internmentEpisodeService;

    public EvolutionNoteValidator(InternmentEpisodeService internmentEpisodeService) {
        this.internmentEpisodeService = internmentEpisodeService;
    }

    @Override
    public void initialize(EvolutionNoteValid constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer internmentEpisodeId = (Integer) parameters[1];
        return !internmentEpisodeService.haveEpicrisis(internmentEpisodeId);
    }
}

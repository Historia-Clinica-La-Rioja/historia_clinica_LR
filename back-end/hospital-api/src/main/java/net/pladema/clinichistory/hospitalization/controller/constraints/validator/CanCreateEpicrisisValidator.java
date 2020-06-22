package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.CanCreateEpicrisis;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class CanCreateEpicrisisValidator implements ConstraintValidator<CanCreateEpicrisis, Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(CanCreateEpicrisisValidator.class);

    private final InternmentEpisodeService internmentEpisodeService;

    public CanCreateEpicrisisValidator(InternmentEpisodeService internmentEpisodeService) {
        this.internmentEpisodeService = internmentEpisodeService;
    }

    @Override
    public void initialize(CanCreateEpicrisis constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> parameters {}", parameters);
        Integer internmentEpisodeId = (Integer)parameters[1];
        return internmentEpisodeService.canCreateEpicrisis(internmentEpisodeId);
    }
}

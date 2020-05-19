package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.CreateUpdateEpicrisisValid;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class CreateUpdateEpicrisisValidator implements ConstraintValidator<CreateUpdateEpicrisisValid, Object[]> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateUpdateEpicrisisValidator.class);

    private final InternmentEpisodeService internmentEpisodeService;

    public CreateUpdateEpicrisisValidator(InternmentEpisodeService internmentEpisodeService) {
        this.internmentEpisodeService = internmentEpisodeService;
    }

    @Override
    public void initialize(CreateUpdateEpicrisisValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> parameters {}", parameters);
        Integer internmentEpisodeId = (Integer)parameters[1];
        return internmentEpisodeService.haveAnamnesisAndEvolutionNote(internmentEpisodeId);
    }
}

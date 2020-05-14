package net.pladema.internation.controller.constraints.validator;

import net.pladema.internation.controller.constraints.EpicrisisValid;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.repository.core.entity.InternmentEpisode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.util.Optional;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class EpicrisisValidator implements ConstraintValidator<EpicrisisValid, Object[]> {

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public EpicrisisValidator(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }

    @Override
    public void initialize(EpicrisisValid constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer internmentEpisodeId = (Integer) parameters[1];
        Optional<InternmentEpisode> internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId);

        return internmentEpisode.isPresent() && !internmentEpisode.get().hasEpicrisisDocument();
    }
}

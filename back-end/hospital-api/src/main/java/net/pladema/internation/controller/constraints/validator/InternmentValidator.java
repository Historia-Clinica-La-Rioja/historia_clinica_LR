package net.pladema.internation.controller.constraints.validator;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class InternmentValidator implements ConstraintValidator<InternmentValid, Object[]> {

    private static final String INTERNMENT_EPISODE_PROPERTY = "internmentEpisodeId";
    private static final String INSTITUTION_PROPERTY = "institutionId";

    private final InternmentEpisodeRepository internmentEpisodeRepository;
    private final InstitutionRepository institutionRepository;

    public InternmentValidator(InternmentEpisodeRepository internmentEpisodeRepository,
                               InstitutionRepository institutionRepository){
        this.internmentEpisodeRepository = internmentEpisodeRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public void initialize(InternmentValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer institutionId = (Integer)parameters[0];
        Integer internmentEpisodeId = (Integer)parameters[1];

        Boolean existsIntenmentEpisode = internmentEpisodeRepository.existsById(internmentEpisodeId);
        Boolean existsInstitution = institutionRepository.existsById(institutionId);

        setResponse(context, "{institution.invalid}", INSTITUTION_PROPERTY);
        setResponse(context, "{internmentepisode.invalid}", INTERNMENT_EPISODE_PROPERTY);

        return existsInstitution && existsIntenmentEpisode;
    }

    private void setResponse(ConstraintValidatorContext constraintValidatorContext,
                             String message,
                             String propertyName) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
}

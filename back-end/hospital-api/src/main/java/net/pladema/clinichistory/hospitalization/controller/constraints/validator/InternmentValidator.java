package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;

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
    	//empty until done
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
        Integer institutionId = (Integer)parameters[0];
        Integer internmentEpisodeId = (Integer)parameters[1];

        boolean existsInstitution = institutionRepository.existsById(institutionId);
        boolean existsInternmentEpisode = internmentEpisodeRepository.existsById(internmentEpisodeId);

        if(existsInstitution)
            setResponse(context, "{institution.invalid}", INSTITUTION_PROPERTY);
        if(existsInternmentEpisode)
            setResponse(context, "{internmentepisode.invalid}", INTERNMENT_EPISODE_PROPERTY);

        return existsInstitution && existsInternmentEpisode;
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

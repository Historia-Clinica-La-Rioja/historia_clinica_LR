package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.sgx.featureflags.AppFeature;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class InternmentDischargeValidator implements ConstraintValidator<InternmentDischargeValid, Integer> {

    private static final String INTERNMENT_EPISODE_PROPERTY = "internmentEpisodeId";

    private final InternmentEpisodeRepository internmentEpisodeRepository;
    
    private final FeatureFlagsService featureFlagService;
    
    public InternmentDischargeValidator(InternmentEpisodeRepository internmentEpisodeRepository, FeatureFlagsService featureFlagService){
        this.internmentEpisodeRepository = internmentEpisodeRepository;
        this.featureFlagService = featureFlagService;
    }

    @Override
    public void initialize(InternmentDischargeValid constraintAnnotation) {
    	//empty until done
    }

    /**
     * El alta de una internación requiere que exista una epicrisis asociada.
     * Si está activo habilitarAltaSinEpicrisis, se ignora la validación.
     */
    @Override
    public boolean isValid(Integer parameters, ConstraintValidatorContext context) {
        Integer internmentEpisodeId = parameters;
        boolean valid = true;
        
        if (!featureFlagService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
        	valid = internmentEpisodeRepository.haveEpicrisis(internmentEpisodeId);
        }
        if (!valid) {
        	setResponse(context, "{internmentdischarge.invalid}", INTERNMENT_EPISODE_PROPERTY);
        }
        return valid;
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

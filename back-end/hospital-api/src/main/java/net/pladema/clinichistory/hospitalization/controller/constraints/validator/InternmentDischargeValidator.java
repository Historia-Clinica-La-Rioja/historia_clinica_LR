package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InternmentDischargeValidator implements ConstraintValidator<InternmentDischargeValid, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentDischargeValidator.class);

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
    public boolean isValid(Integer internmentEpisodeId, ConstraintValidatorContext context) {
        LOG.debug("Going to Validate with InternmentDischargeValid");
        boolean valid = true;

        if (!featureFlagService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
            valid = internmentEpisodeRepository.hasFinalEpicrisis(internmentEpisodeId);
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

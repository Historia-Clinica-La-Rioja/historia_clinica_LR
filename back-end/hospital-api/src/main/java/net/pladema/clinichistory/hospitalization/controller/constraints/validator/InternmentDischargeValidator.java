package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@RequiredArgsConstructor
public class InternmentDischargeValidator implements ConstraintValidator<InternmentDischargeValid, Integer> {

    private static final String INTERNMENT_EPISODE_PROPERTY = "internmentEpisodeId";

    private final InternmentEpisodeRepository internmentEpisodeRepository;
    
    private final FeatureFlagsService featureFlagService;

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
        log.debug("Going to Validate with InternmentDischargeValid");
        boolean valid = true;

        if (!featureFlagService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
            valid = internmentEpisodeRepository.hasFinalEpicrisis(internmentEpisodeId);
        }

        if (!valid) {
            setResponse(context, "{internmentdischarge.invalid.no-epicrisis}", INTERNMENT_EPISODE_PROPERTY);
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

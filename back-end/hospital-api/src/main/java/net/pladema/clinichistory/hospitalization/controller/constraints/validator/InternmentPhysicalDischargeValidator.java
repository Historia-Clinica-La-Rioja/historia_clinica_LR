package net.pladema.clinichistory.hospitalization.controller.constraints.validator;

import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentPhysicalDischargeValid;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InternmentPhysicalDischargeValidator implements ConstraintValidator<InternmentPhysicalDischargeValid, Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(InternmentDischargeValidator.class);

	private static final String INTERNMENT_EPISODE_PROPERTY = "internmentEpisodeId";

	private final InternmentEpisodeRepository internmentEpisodeRepository;

	private final FeatureFlagsService featureFlagService;

	public InternmentPhysicalDischargeValidator(InternmentEpisodeRepository internmentEpisodeRepository, FeatureFlagsService featureFlagService){
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.featureFlagService = featureFlagService;
	}

	@Override
	public void initialize(InternmentPhysicalDischargeValid constraintAnnotation) {
		//empty until done
	}

	/**
	 * El alta física de una internación requiere que exista una anamnesis asociada.
	 */
	@Override
	public boolean isValid(Integer internmentEpisodeId, ConstraintValidatorContext context) {
		LOG.debug("Going to Validate with InternmentPhysicalDischargeValid");
		boolean valid = internmentEpisodeRepository.haveAnamnesis(internmentEpisodeId);
		if (!valid) {
			setResponse(context, "{internmentphysicaldischarge.invalid}", INTERNMENT_EPISODE_PROPERTY);
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

package ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ChangePrescriptionStateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrescriptionStatusValidator implements ConstraintValidator<ValidPrescriptionStatus, ChangePrescriptionStateDto> {

	private static final Logger LOG = LoggerFactory.getLogger(PrescriptionStatusValidator.class);

	@Override
	public void initialize(ValidPrescriptionStatus constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(ChangePrescriptionStateDto changePrescriptionLineDto, ConstraintValidatorContext constraintValidatorContext) {
		return false;
	}


	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
	}
}

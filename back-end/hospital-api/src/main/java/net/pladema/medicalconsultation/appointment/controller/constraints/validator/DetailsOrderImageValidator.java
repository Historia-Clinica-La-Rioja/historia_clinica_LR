package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidDetailsOrderImage;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@RequiredArgsConstructor
@Slf4j
public class DetailsOrderImageValidator implements ConstraintValidator<ValidDetailsOrderImage, Object[]> {
	private final AppointmentOrderImageService appointmentOrderImageService;

	@Override
	public void initialize(ValidDetailsOrderImage constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
		Integer appointmentId = (Integer) parameters[1];
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		boolean result = true;

		if (appointmentOrderImageService.isAlreadyCompleted(appointmentId)) {
			log.debug("Trying to finish appointmentId {} study already finished by a technician", appointmentId);
			buildResponse(context, "{appointment.study.already.finished}");
			result = false;
		}
		return result;
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}

package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PrescriptionStatusValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrescriptionStatus {

	String message() default "Error de validación";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = HeadCircumferenceValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeadCircumferenceDataValid {

	String message() default "{diagnosis.anthropometric.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

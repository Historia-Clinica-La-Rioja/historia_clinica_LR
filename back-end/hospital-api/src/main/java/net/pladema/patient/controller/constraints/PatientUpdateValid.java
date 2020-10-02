package net.pladema.patient.controller.constraints;

import net.pladema.patient.controller.constraints.validator.PatientUpdateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatientUpdateValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatientUpdateValid {

    String message() default "{patient.update.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package net.pladema.patient.controller.constraints;

import net.pladema.patient.controller.constraints.validator.FilterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilterValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterValid {

    String message() default "{patient.filter.null}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

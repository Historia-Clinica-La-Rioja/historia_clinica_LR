package net.pladema.internation.controller.ips.constraints;

import net.pladema.internation.controller.ips.constraints.validator.BeforeOrPresentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BeforeOrPresentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeOrPresent {

    String message() default "{clinicalobservation.effetivetime.future}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

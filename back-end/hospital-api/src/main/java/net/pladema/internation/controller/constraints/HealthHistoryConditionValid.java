package net.pladema.internation.controller.constraints;

import net.pladema.internation.controller.constraints.validator.HealthHistoryConditionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HealthHistoryConditionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HealthHistoryConditionValid {

    String message() default "{health.history.condition.repeated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

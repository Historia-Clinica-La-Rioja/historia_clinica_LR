package net.pladema.internation.controller.constraints;

import net.pladema.internation.controller.constraints.validator.DiagnosisValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DiagnosisValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiagnosisValid {

    String message() default "{diagnosis.repeated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

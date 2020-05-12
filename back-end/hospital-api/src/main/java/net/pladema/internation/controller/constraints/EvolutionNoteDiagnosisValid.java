package net.pladema.internation.controller.constraints;

import net.pladema.internation.controller.constraints.validator.EvolutionNoteDiagnosisValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EvolutionNoteDiagnosisValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EvolutionNoteDiagnosisValid {

    String message() default "{diagnosis.secondary.repeated.principal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

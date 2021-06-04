package net.pladema.clinichistory.ips.controller.constraints;

import net.pladema.clinichistory.ips.controller.constraints.validator.HeightDataValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HeightDataValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeightDataValid {

    String message() default "{diagnosis.anthropometric.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

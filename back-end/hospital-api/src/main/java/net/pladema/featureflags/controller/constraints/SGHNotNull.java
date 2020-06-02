package net.pladema.featureflags.controller.constraints;

import net.pladema.featureflags.controller.constraints.validators.SGHNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SGHNotNullValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SGHNotNull {

    String message() default "{attribute.required}";

    String[] ffs();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

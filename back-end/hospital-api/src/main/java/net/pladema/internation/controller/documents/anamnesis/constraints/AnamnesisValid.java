package net.pladema.internation.controller.documents.anamnesis.constraints;

import net.pladema.internation.controller.documents.anamnesis.constraints.validator.AnamnesisValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AnamnesisValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnamnesisValid {

    String message() default "{anamnesis.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package net.pladema.internation.controller.constraints;

import net.pladema.internation.controller.constraints.validator.UpdateDocumentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UpdateDocumentValidator.class)
@Target( { ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateDocumentValid {

    String message() default "{document.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

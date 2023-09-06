package net.pladema.template.infrastructure.input.rest.constraints;

import net.pladema.template.infrastructure.input.rest.constraints.validator.DocumentTemplateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DocumentTemplateValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocumentTemplate {
    String message() default "{document.template.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

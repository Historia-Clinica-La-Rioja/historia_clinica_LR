package net.pladema.template.infrastructure.input.rest.constraints;

import net.pladema.template.infrastructure.input.rest.constraints.validator.TypeDocumentTemplateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TypeDocumentTemplateValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTypeDocumentTemplate {

    String message() default "{document.template.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

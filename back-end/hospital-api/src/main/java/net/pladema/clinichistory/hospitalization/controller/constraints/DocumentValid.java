package net.pladema.clinichistory.hospitalization.controller.constraints;

import net.pladema.clinichistory.hospitalization.controller.constraints.validator.DocumentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DocumentValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentValid {

    String message() default "{document.invalid}";

    boolean isConfirmed();

    short documentType();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

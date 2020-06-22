package net.pladema.clinichistory.hospitalization.controller.constraints;

import net.pladema.clinichistory.hospitalization.controller.constraints.validator.CanCreateEpicrisisValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CanCreateEpicrisisValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanCreateEpicrisis {

    String message() default "{epicrisis.document.create.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

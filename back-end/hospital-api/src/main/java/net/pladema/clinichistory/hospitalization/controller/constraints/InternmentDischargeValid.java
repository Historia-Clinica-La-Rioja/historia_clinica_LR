package net.pladema.clinichistory.hospitalization.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.clinichistory.hospitalization.controller.constraints.validator.InternmentDischargeValidator;

@Documented
@Constraint(validatedBy = InternmentDischargeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InternmentDischargeValid {

    String message() default "{internmentdischarge.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
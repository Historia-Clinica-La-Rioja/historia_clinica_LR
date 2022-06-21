package net.pladema.clinichistory.hospitalization.controller.constraints;

import net.pladema.clinichistory.hospitalization.controller.constraints.validator.InternmentPhysicalDischargeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = InternmentPhysicalDischargeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InternmentPhysicalDischargeValid {

	String message() default "{internmentphysicaldischarge.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

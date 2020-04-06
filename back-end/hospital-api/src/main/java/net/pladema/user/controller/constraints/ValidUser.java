package net.pladema.user.controller.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUserValidator.class)
public @interface ValidUser {
	
	String message() default "{invalid.user}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
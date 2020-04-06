package net.pladema.security.authentication.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnabledUserValidator.class)
public @interface EnabledUser {
	
	String message() default "{disabled.user}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
package net.pladema.security.authentication.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VerificationTokenValidator.class)
public @interface VerificationToken {
	
	String message() default "{jwt.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
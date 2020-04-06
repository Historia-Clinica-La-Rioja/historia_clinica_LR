package net.pladema.permissions.controller.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistRoleValidator.class)
public @interface ExistRole {
	
	String message() default "{role.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
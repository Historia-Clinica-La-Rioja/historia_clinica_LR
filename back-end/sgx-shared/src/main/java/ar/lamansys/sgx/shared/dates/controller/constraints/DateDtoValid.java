package ar.lamansys.sgx.shared.dates.controller.constraints;

import ar.lamansys.sgx.shared.dates.controller.constraints.validator.DateDtoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = DateDtoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateDtoValid {

	String message() default "{date-dto.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

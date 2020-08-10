package net.pladema.medicalconsultation.diary.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.ExistingDiaryPeriodValidator;

@Documented
@Constraint(validatedBy = ExistingDiaryPeriodValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingDiaryPeriodValid {

    String message() default "{diary.period.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

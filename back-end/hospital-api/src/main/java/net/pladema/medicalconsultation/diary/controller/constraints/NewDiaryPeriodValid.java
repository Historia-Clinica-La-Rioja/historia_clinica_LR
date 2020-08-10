package net.pladema.medicalconsultation.diary.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.NewDiaryPeriodValidator;

@Documented
@Constraint(validatedBy = NewDiaryPeriodValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewDiaryPeriodValid {

    String message() default "{diary.period.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

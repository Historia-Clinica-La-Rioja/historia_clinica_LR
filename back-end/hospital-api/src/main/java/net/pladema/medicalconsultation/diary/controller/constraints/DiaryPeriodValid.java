package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryPeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DiaryPeriodValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiaryPeriodValid {

    String message() default "{diary.period.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

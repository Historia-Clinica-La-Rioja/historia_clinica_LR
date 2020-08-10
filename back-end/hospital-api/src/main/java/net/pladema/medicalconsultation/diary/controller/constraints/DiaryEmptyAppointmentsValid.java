package net.pladema.medicalconsultation.diary.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryEmptyAppointmentsValidator;

@Documented
@Constraint(validatedBy = DiaryEmptyAppointmentsValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiaryEmptyAppointmentsValid {

    String message() default "{diary.appointments.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

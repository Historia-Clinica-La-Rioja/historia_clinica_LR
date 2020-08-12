package net.pladema.medicalconsultation.diary.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryDeleteableAppointmentsValidator;

@Documented
@Constraint(validatedBy = DiaryDeleteableAppointmentsValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiaryDeleteableAppoinmentsValid {

    String message() default "{diary.appointments.invalid.cancelled}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

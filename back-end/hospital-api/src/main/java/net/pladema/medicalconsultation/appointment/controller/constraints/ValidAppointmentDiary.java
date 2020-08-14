package net.pladema.medicalconsultation.appointment.controller.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import net.pladema.medicalconsultation.appointment.controller.constraints.validator.AppointmentDiaryValidator;

@Documented
@Constraint(validatedBy = AppointmentDiaryValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAppointmentDiary {

    String message() default "{appointment.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package net.pladema.medicalconsultation.appointment.controller.constraints;

import net.pladema.medicalconsultation.appointment.controller.constraints.validator.AppointmentStateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AppointmentStateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAppointmentState {

    String message() default "{appointment.state.id.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

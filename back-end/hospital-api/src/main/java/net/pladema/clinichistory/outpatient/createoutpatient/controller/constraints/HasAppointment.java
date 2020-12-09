package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.validator.HasAppointmentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HasAppointmentValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAppointment {

    String message() default "{outpatient.appointment.valid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

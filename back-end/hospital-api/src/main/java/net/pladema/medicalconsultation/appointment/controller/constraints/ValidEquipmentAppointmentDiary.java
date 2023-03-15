package net.pladema.medicalconsultation.appointment.controller.constraints;

import net.pladema.medicalconsultation.appointment.controller.constraints.validator.EquipmentAppointmentDiaryValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EquipmentAppointmentDiaryValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEquipmentAppointmentDiary {

    String message() default "{appointment.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

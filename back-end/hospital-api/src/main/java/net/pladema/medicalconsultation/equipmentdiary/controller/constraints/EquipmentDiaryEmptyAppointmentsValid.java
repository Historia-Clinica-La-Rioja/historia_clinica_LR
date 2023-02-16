package net.pladema.medicalconsultation.equipmentdiary.controller.constraints;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator.EquipmentDiaryEmptyAppointmentsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EquipmentDiaryEmptyAppointmentsValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EquipmentDiaryEmptyAppointmentsValid {

	String message() default "{diary.appointments.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

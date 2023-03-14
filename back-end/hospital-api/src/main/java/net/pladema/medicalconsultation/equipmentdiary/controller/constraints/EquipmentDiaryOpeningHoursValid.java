package net.pladema.medicalconsultation.equipmentdiary.controller.constraints;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator.EquipmentDiaryOpeningHoursValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EquipmentDiaryOpeningHoursValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EquipmentDiaryOpeningHoursValid {

    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

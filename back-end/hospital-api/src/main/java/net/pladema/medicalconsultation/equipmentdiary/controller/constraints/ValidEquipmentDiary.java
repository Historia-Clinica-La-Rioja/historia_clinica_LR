package net.pladema.medicalconsultation.equipmentdiary.controller.constraints;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator.EquipmentDiaryValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EquipmentDiaryValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEquipmentDiary {

    String message() default "{diary.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


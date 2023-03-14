package net.pladema.medicalconsultation.equipmentdiary.controller.constraints;



import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.validator.NewEquipmentDiaryPeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NewEquipmentDiaryPeriodValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewDiaryPeriodValid {

    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

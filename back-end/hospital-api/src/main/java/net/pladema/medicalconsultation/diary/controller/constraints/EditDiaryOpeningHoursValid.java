package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.EditDiaryOpeningHoursValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EditDiaryOpeningHoursValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EditDiaryOpeningHoursValid {

    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

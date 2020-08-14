package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DiaryValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiary {

    String message() default "{diary.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


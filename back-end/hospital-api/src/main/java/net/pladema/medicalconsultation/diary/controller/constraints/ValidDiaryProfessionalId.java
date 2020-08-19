package net.pladema.medicalconsultation.diary.controller.constraints;

import net.pladema.medicalconsultation.diary.controller.constraints.validator.DiaryProfessionalIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DiaryProfessionalIdValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiaryProfessionalId {

    String message() default "{healthcare-professional.id.error.validation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


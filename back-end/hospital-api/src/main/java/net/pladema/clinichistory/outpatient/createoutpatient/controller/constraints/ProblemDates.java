package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProblemDatesValidator.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProblemDates {

    String message() default "{problem.dates.not.after}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

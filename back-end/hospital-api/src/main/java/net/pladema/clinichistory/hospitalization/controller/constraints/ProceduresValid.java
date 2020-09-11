package net.pladema.clinichistory.hospitalization.controller.constraints;


import net.pladema.clinichistory.hospitalization.controller.constraints.validator.ProceduresValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProceduresValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProceduresValid {

    String message() default "{procedures.repeated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

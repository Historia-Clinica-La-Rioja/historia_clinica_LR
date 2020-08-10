package net.pladema.clinichistory.hospitalization.controller.generalstate.constraint;

import net.pladema.clinichistory.hospitalization.controller.generalstate.constraint.validator.EffectiveVitalSignTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EffectiveVitalSignTimeValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EffectiveVitalSignTimeValid {

    String message() default "{clinical.observation.effective.time.mandatory}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints;

import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.validator.EvolutionNoteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EvolutionNoteValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EvolutionNoteValid {

    String message() default "{epicrisis.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

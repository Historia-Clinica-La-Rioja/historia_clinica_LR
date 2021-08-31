<<<<<<< HEAD:back-end/clinic-history/src/main/java/ar/lamansys/sgh/clinichistory/infrastructure/input/rest/ips/constraints/HeightDataValid.java
package net.pladema.clinichistory.ips.controller.constraints;

import net.pladema.clinichistory.ips.controller.constraints.validator.HeightDataValidator;
=======
package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints;
>>>>>>> ff8c5eff6b5c05942dbfa19e04814441f6ecd2f8:back-end/hospital-api/src/main/java/net/pladema/clinichistory/ips/controller/constraints/AnthropometricDataValid.java

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HeightDataValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeightDataValid {

    String message() default "{diagnosis.anthropometric.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

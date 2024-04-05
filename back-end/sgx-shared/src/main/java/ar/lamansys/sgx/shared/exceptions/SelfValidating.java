package ar.lamansys.sgx.shared.exceptions;


import ar.lamansys.sgx.shared.validation.CustomContainerValueExtractor;

import javax.validation.*;
import java.util.Set;

public abstract class SelfValidating<T> {

  private Validator validator;

  public SelfValidating() {
    ValidatorFactory factory = Validation.byDefaultProvider()
			.configure()
			.addValueExtractor(new CustomContainerValueExtractor())
			.buildValidatorFactory();
    validator = factory.getValidator();
  }

  /**
   * Evaluates all Bean Validations on the attributes of this
   * instance.
   */
  public void validateSelf() {
    Set<ConstraintViolation<T>> violations = validator.validate((T) this);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
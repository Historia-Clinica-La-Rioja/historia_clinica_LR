package net.pladema.sgx.validation;

import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

public abstract class ValidationContextSetup {

	@Mock
	protected ConstraintValidatorContext contextMock;
	
	@Mock
	protected ConstraintViolationBuilder mockConstraintViolationBuilder;
	
	@Mock
	protected NodeBuilderDefinedContext mockNodeBuilderDefinedContext;
	
}

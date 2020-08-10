package net.pladema.sgx.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class ValidationContextSetup {

	@MockBean
	protected ConstraintValidatorContext contextMock;
	
	@MockBean
	protected ConstraintViolationBuilder mockConstraintViolationBuilder;
	
	@MockBean
	protected NodeBuilderDefinedContext mockNodeBuilderDefinedContext;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		when(contextMock.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		when(mockConstraintViolationBuilder.addNode(any())).thenReturn(mockNodeBuilderDefinedContext);

	}
	
	
}

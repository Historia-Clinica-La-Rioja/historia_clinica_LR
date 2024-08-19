package ar.lamansys.sgx.shared.validation;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Configuration;

public class CustomLocalValidatorFactoryBean extends LocalValidatorFactoryBean {

	@Override
	protected void postProcessConfiguration(Configuration<?> configuration) {
		super.postProcessConfiguration(configuration);
		configuration.addValueExtractor(new CustomContainerValueExtractor());
	}

}

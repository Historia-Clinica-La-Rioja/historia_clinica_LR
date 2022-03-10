package ar.lamansys.sgx.auth;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.i18n.CustomMessageSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;

import java.util.Locale;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class})
public class IntegrationController {
	
	@Autowired
	private MessageSource messageSource;

	protected String buildMessage(String keyMessage){
		return messageSource.getMessage(keyMessage, null, Locale.getDefault());
	}
}
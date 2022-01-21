package ar.lamansys.sgx.auth;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.ActuatorConfiguration;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.i18n.CustomMessageSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class, TestSecurityConfiguration.class})
public class IntegrationTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected MessageSource messageSource;

	protected String buildMessage(String keyMessage){
		return messageSource.getMessage(keyMessage, null, Locale.getDefault());
	}
}
package net.pladema.sgh.app;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.ActuatorConfiguration;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.i18n.CustomMessageSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class, TestSecurityConfiguration.class})
public class IntegrationTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected void buildMockMvc() {
		//Init MockMvc Object and build
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	protected String buildMessage(String keyMessage){
		return messageSource.getMessage(keyMessage, null, Locale.getDefault());
	}
}
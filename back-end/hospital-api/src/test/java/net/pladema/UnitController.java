package net.pladema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import net.pladema.security.service.SecurityService;
import net.pladema.security.token.service.TokenService;
import ar.lamansys.sgx.shared.configuration.ActuatorConfiguration;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.i18n.CustomMessageSourceConfiguration;

import java.util.Locale;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class})
public class UnitController {
	
    @Autowired
    protected MockMvc mockMvc;

	@Autowired
	private MessageSource messageSource;

	@MockBean
	protected SecurityService securityService;
	
	@MockBean
	protected TokenService tokenService;

	protected String buildMessage(String keyMessage){
		return messageSource.getMessage(keyMessage, null, Locale.getDefault());
	}
}
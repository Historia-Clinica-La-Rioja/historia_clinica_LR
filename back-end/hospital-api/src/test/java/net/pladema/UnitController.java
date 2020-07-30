package net.pladema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import net.pladema.security.service.SecurityService;
import net.pladema.security.token.service.TokenService;
import net.pladema.sgx.actuator.configuration.ActuatorConfiguration;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.sgx.i18n.CustomMessageSourceConfiguration;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class})
public class UnitController {
	
    @Autowired
    protected MockMvc mockMvc;
	
	@MockBean
	protected SecurityService securityService;
	
	@MockBean
	protected TokenService tokenService;
	
}